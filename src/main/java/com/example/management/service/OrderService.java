package com.example.management.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.management.form.OrderDetailForm;
import com.example.management.form.OrderForm;
import com.example.management.mapper.CustomerHistoryMapper;
import com.example.management.mapper.EmployeeHistoryMapper;
import com.example.management.mapper.ItemHistoryMapper;
import com.example.management.mapper.OrderDetailMapper;
import com.example.management.mapper.OrderMapper;
import com.example.management.mapper.ShippingMapper;
import com.example.management.model.Order;
import com.example.management.model.OrderDetail;

/**
 * 受注のロジッククラス
 */
@Service
public class OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmployeeHistoryMapper employeeHistoryMapper;
	
	@Autowired
	private CustomerHistoryMapper customerHistoryMapper;
	
	@Autowired
	private ItemHistoryMapper itemHistoryMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	private ShippingMapper shippingMapper;

	/**
	 * 受注テーブルの全レコード数をInteger型で取得し、
	 * その値に+1をした上で[00000000」の形に整形する。
	 * 
	 * @return orderNumber 受注番号
	 */
	public String getOrderNumber() {
		
		// 受注テーブルの全レコード数 + 1
		Integer tempOrderNumber = orderMapper.countAll().orElse(0) + 1;
		
		//　「00000000」の形の文字列に整形。
		String orderNumber = String.format("%08d", tempOrderNumber);
			
		return orderNumber;
	}
	
	/**
	 * 受注・受注明細テーブルへ新規にinsert処理するメソッド。
	 * 
	 * @param orderForm 受注フォーム
	 * @param employeeId 社員ID
	 */
	@Transactional(rollbackForClassName = {"Exception"})
	public void create(OrderForm orderForm, Long employeeId) {
		
		// 受注フォームの情報を受注クラスにマッピングする。
		Order order = new Order();
		order.setOrderNumber(orderForm.getOrderNumber());
		
		// 顧客履歴テーブルから引数の顧客IDを基に、更新日時が新しいレコード情報を取得し、受注クラスにセットする。
		order.setCustomerHistory(customerHistoryMapper.findByCustomerIdByLatest(orderForm.getCustomerId()));
		
		// 社員履歴テーブルから引数の社員IDを基に、更新日時が新しいレコード情報を取得し、受注クラスにセットする。
		order.setEmployeeHistory(employeeHistoryMapper.findByEmployeeIdByLatest(employeeId));
		
		// 受注テーブルへ1件挿入処理。
		orderMapper.create(order);
			
		
		
		// 受注フォームから受注明細フォームを取り出し、変数に代入する。
		List<OrderDetailForm> orderDetailForms = orderForm.getOrderDetailForms();
		
		// 繰り返し文の結果を格納する受注明細クラス。
		List<OrderDetail> orderDetails = new ArrayList<>();
		
		// for文の中で使用する明細番号
		Integer detailId = 1; 
		
		// 受注明細フォームのリストに対して繰り返し処理
		for (OrderDetailForm orderDetailForm : orderDetailForms) {
			
			// 受注明細フォームの情報を受注明細クラスにマッピングする。
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderQuantity(orderDetailForm.getOrderQuantity());
			
			// 受注フォームの受注番号をセットする。
			orderDetail.setOrderNumber(orderForm.getOrderNumber());
			
			// 受注明細フォームの商品IDを基に、商品履歴テーブルから更新日時が最新のレコードを取得しセットする。
			orderDetail.setItemHistory(itemHistoryMapper.findByItemIdByLatest(orderDetailForm.getItemId()));
			
			// 明細番号をセット
			orderDetail.setDetailId(detailId);
			
			// 格納
			orderDetails.add(orderDetail);
			
			detailId ++;
		}
		
		// 格納し終わった受注明細クラスのリストを引数としてDBへinsert処理。
		orderDetailMapper.createAll(orderDetails);
	}
	
	/**
	 * 受注明細フォームに入力された値を受注明細テーブルに反映させる。
	 * 明細行全てが削除される場合、受注テーブルの該当行も削除(論理)する。
	 * 明細行が「受注数 = 出荷数」となった場合、受注明細テーブルの該当行の出荷完了日を更新する。
	 * 全ての明細行が出荷完了となった場合、受注テーブルの該当行の出荷完了日を更新する。
	 * 
	 * @param orderForm 受注フォーム
	 */
	@Transactional(rollbackForClassName = "{Exception}")
	public void update(OrderForm orderForm) {
		
		// for内で使用する受注明細フォームと受注明細クラスのリスト用意。
		List<OrderDetailForm> orderDetailForms = orderForm.getOrderDetailForms();
		
		// for内で全ての行が削除されているかを判別するフラグ。
		Short removeAllFlag = 1;
		
		// for内で全ての行が出荷完了かどうかを判別するフラグ。
		Short completionFlag = 1;
		
		// for内で受注明細フォームから受注明細クラスへマッピングする。
		for (OrderDetailForm orderDetailForm : orderDetailForms) {
			
			// モデルマッパーによる値の詰め替え
			OrderDetail orderDetail = modelMapper.map(orderDetailForm, OrderDetail.class);
			
			// 受注フォームの受注番号をセットする。
			orderDetail.setOrderNumber(orderForm.getOrderNumber());
		
			// 受注明細クラスを渡し、DBをupdateする。
			orderDetailMapper.update(orderDetail);

			// 削除されていなければ、全削除フラグを「0」にする。
			if (orderDetail.getInvalid() == 0) {
				
				removeAllFlag = 0;
			}
			
			// 出荷済計を取得。
			Integer shippingQuantityTotal = shippingMapper.sumOfShippingQuantityTotal(orderForm.getOrderNumber(), orderDetailForm.getDetailId()).orElse(0);
			
			// フォームの受注数を取得。
			Integer orderQuantity = orderDetailForm.getOrderQuantity();
			
			// 「出荷数計 = 受注数」になった場合、出荷完了とする。
			if (shippingQuantityTotal == orderQuantity) {
				
				// 出荷完了日を更新。
				orderDetailMapper.updateCompletion(orderDetail);
			} else {
				
				// 全明細行が出荷完了ではない為、フラグを0にする。
				completionFlag = 0;
			}
			
		}
		
		// 明細行が全削除されていれば、受注テーブルの該当行自体も削除(論理)とする。
		if (removeAllFlag == 1) {
			
			Order order = new Order();
			
			// 該当行を識別するためのIDをセット。
			order.setId(orderForm.getId());
			
			// 受注テーブルへの更新処理。(論理削除)
			orderMapper.updateInvalid(order);
		}
		
		// 明細行が全完了されていれば、受注テーブルの該当行自体も出荷完了とする。
		if (completionFlag == 1) {
			
			Order order = new Order();
			
			// 該当行を識別するためのIDをセット。
			order.setId(orderForm.getId());
			
			// 受注テーブルへの更新処理。(出荷完了日更新)
			orderMapper.updateCompletion(order);
		}
	}
	
	/**
	 * 受注IDを基に、受注テーブルの該当レコードを削除(論理)する。
	 * また、対応する受注明細テーブルのレコードも同様に削除する。
	 * 
	 * @param order 受注クラス
	 */
	@Transactional(noRollbackForClassName = "{Exception}")
	public void delete(Order order) {
		
		// forで使用する受注明細
		List<OrderDetail> orderDetails = order.getOrderDetails();
		
		// 受注明細分だけ、繰り返し処理。
		for (OrderDetail orderDetail : orderDetails) {
			
			// 各明細に削除フラグを立てる。
			orderDetail.setInvalid((short)1);
			
			// 受注明細テーブルを全て削除(論理)として更新。
			orderDetailMapper.update(orderDetail);
		}
		
		// 受注テーブルの更新処理。(論理削除)
		orderMapper.updateInvalid(order);
	}
	
	/**
	 * 引数の受注フォームに、受注クラスから情報を取得しセットするメソッド。
	 * 
	 * @param order 受注クラス
	 * @param orderForm 受注フォーム
	 * 
	 * @return orderForm 全ての情報をセットした受注フォーム
	 */
	public OrderForm formMapping(Order order) {
		
		// 受注クラスから受注フォームへ値の詰め替え。
		OrderForm orderForm = modelMapper.map(order, OrderForm.class);

		// 受注明細リストを受注明細フォームに詰め替える為のリスト用意。
		List<OrderDetail> orderDetails = order.getOrderDetails();
		List<OrderDetailForm> orderDetailForms = new ArrayList<>();
		
		// for内で値の詰め替え
		for (OrderDetail orderDetail : orderDetails) {
			
			// モデルマッパーによる詰め替え
			OrderDetailForm orderDetailForm = modelMapper.map(orderDetail, OrderDetailForm.class);
			
			// 商品情報から単価を取得する。
			Long unitPrice = orderDetail.getItemHistory().getUnitPrice();
			
			// 単価をセットする。
			orderDetailForm.setUnitPrice(unitPrice);
			
			// 受注小計を計算する。
			Long orderAmount = unitPrice * orderDetail.getOrderQuantity();
			
			// 受注小計をセット。
			orderDetailForm.setOrderAmount(orderAmount);
			
			// 出荷完了日をセット
			orderDetailForm.setCompletionDate(orderDetail.getCompletionDate());
			
			// フォームを格納
			orderDetailForms.add(orderDetailForm);
		}
		
		// 格納した受注明細フォーム情報をセットする。
		orderForm.setOrderDetailForms(orderDetailForms);
		
		return orderForm;
	}
}
