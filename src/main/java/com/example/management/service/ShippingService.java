package com.example.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.management.form.ShippingDetailForm;
import com.example.management.form.ShippingForm;
import com.example.management.mapper.OrderDetailMapper;
import com.example.management.mapper.OrderMapper;
import com.example.management.mapper.ShippingMapper;
import com.example.management.mapper.StockMapper;
import com.example.management.model.CustomerHistory;
import com.example.management.model.ItemHistory;
import com.example.management.model.Order;
import com.example.management.model.OrderDetail;
import com.example.management.model.Shipping;

/**
 * 出荷のロジッククラス
 */
@Service
public class ShippingService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	private ShippingMapper shippingMapper;
	
	@Autowired
	private StockMapper stockMapper;

	/**
	 * 引数で受け取った出荷フォームの受注番号を基に、
	 * 各種情報を出荷フォームに詰めるメソッド。
	 * 
	 * @param shippingForm　出荷フォーム
	 * 
	 * @return shippingForm 必要な情報を詰めた出荷フォーム
	 */
	public ShippingForm setShippingForm(ShippingForm shippingForm) {
		
		// 選択された受注番号を取り出す。
		String selectOrderNumber = shippingForm.getSelectOrderNumber();
		
		// 取り出した受注番号を基に、受注テーブルから情報を取得する。
		Order order = orderMapper.findByOrderNumberExcludeInvalidAndCompletion(selectOrderNumber);
		
		// 顧客情報を取り出す。
		CustomerHistory customerHistory = order.getCustomerHistory();
		
		// 顧客履歴情報から、顧客名、住所をセットする。
		shippingForm.setOrderNumber(selectOrderNumber);
		shippingForm.setCustomerName(customerHistory.getName());
		shippingForm.setFirstAddress(customerHistory.getFirstAddress());
		shippingForm.setSecondAddress(customerHistory.getSecondAddress());
		shippingForm.setThirdAddress(customerHistory.getThirdAddress());
		
		// 受注明細情報を取り出す。
		List<OrderDetail> orderDetails = order.getOrderDetails();
		
		// for内で、このリストに明細情報をセットする。
		List<ShippingDetailForm> shippingDetailForms = new ArrayList<>();
		
		// 受注明細行毎に、各種情報を出荷明細フォームにセットする。削除済み、出荷完了済みは除外する。
		for (OrderDetail orderDetail : orderDetails) {
			
			// 受注明細が削除済み若しくは出荷完了済みであれば、出荷明細フォームにセットしない。
			if (orderDetail.getInvalid() == 0 && orderDetail.getCompletionDate() == null) {
				
				// 出荷明細フォームを準備
				ShippingDetailForm shippingDetailForm = new ShippingDetailForm();
				
				// 商品履歴を取り出す。
				ItemHistory itemHistory = orderDetail.getItemHistory();
				
				// 各種情報をセットする。
				shippingDetailForm.setDetailId(orderDetail.getDetailId());
				shippingDetailForm.setItemId(itemHistory.getItemId());
				shippingDetailForm.setItemCode(itemHistory.getCode());
				shippingDetailForm.setItemName(itemHistory.getName());
				shippingDetailForm.setUnitPrice(itemHistory.getUnitPrice());
				shippingDetailForm.setOrderQuantity(orderDetail.getOrderQuantity());
				shippingDetailForm.setShippingQuantityTotal(shippingMapper.sumOfShippingQuantityTotal(order.getOrderNumber(), orderDetail.getDetailId()).orElse(0));
				shippingDetailForm.setActualQuantity(stockMapper.getActualQuantity(itemHistory.getItemId()));
				
				// 格納する。
				shippingDetailForms.add(shippingDetailForm);
			}		
		}
		
		// 格納し終わった出荷明細フォームを出荷フォームにセットする。
		shippingForm.setShippingDetailForms(shippingDetailForms);
		
		return shippingForm;
	}
	
	/**
	 * 出荷テーブルへinsert
	 * 在庫テーブルをupdate(実在庫を減らす)
	 * 受注テーブル・受注明細テーブルをupdate(出荷完了した場合に完了日を更新)
	 * エラーが発生した場合はロールバックする。
	 * 
	 * @param shippingForm 出荷フォーム
	 */
	@Transactional(rollbackForClassName = {"Exception"})
	public void create(ShippingForm shippingForm) {
		
		// 受注番号を取得
		String orderNumber = shippingForm.getOrderNumber();
		
		// 出荷フォームから出荷明細フォームを取得する。
		List<ShippingDetailForm> shippingDetailForms = shippingForm.getShippingDetailForms();
		
		// for文で出荷明細フォームを回す。
		for (ShippingDetailForm shippingDetailForm : shippingDetailForms) {
			
			// 出荷数を取得。
			Integer shippingQuantity = shippingDetailForm.getShippingQuantity();
			
			// 出荷数がnullもしくは0の行は処理を行わない。
			if (shippingQuantity == null || shippingQuantity == 0) {
				
				continue;
			}
			
			// 出荷クラスを用意
			Shipping shipping = new Shipping();
			
			// 受注明細番号を取得
			Integer detailId = shippingDetailForm.getDetailId();
			
			// 受注数を取得する。
			Integer orderQuantity = orderDetailMapper.getOrderQuantity(orderNumber, detailId);
			
			// 出荷済計を取得する。
			Integer shippingQuantityTotal = shippingMapper.sumOfShippingQuantityTotal(orderNumber, detailId).orElse(0);
			
			// 出荷クラスのinsertに必要な情報をセット。
			shipping.setOrderDetail(orderDetailMapper.findByPrimaryKey(orderNumber, detailId));
			shipping.setShippingQuantity(shippingQuantity);
			
			// 出荷テーブルにinsert処理。
			shippingMapper.create(shipping);
			
			// 在庫テーブルをupdate処理。
			stockMapper.updateShipping(shipping);
			
			// 「受注数 - 出荷数計 =　出荷数」となった場合、出荷完了とし、受注明細テーブルの該当行の出荷完了日を更新する。
			if (orderQuantity - shippingQuantityTotal == shippingQuantity) {
				
				// 受注明細クラスを用意。
				OrderDetail orderDetail = new OrderDetail();
				
				// 受注番号・明細番号をセットする。
				orderDetail.setOrderNumber(orderNumber);
				orderDetail.setDetailId(detailId);
				
				// 受注明細テーブルを更新する。
				orderDetailMapper.updateCompletion(orderDetail);
			}
		}	
		
		// 現在の受注明細テーブルを取得。
		List<OrderDetail> tempOrderDetails = orderDetailMapper.findByOrderNumberExcludeInvalidAndCompletion(shippingForm.getOrderNumber());
		
		// 現在の受注明細行(削除・完了済みを除く)の件数と、前のfor内でカウントした完了行の件数が一致した場合、
		// 「今回の出荷登録で、受注番号に紐づく全ての出荷が完了」とみなし、受注テーブルの出荷完了日を更新する。
		if (tempOrderDetails.size() == 0) {
			
			// 受注番号を基に、合致する受注情報を取得する。
			Order order = orderMapper.findByOrderNumberExcludeInvalidAndCompletion(orderNumber);
			
			// 受注テーブルを更新する。
			orderMapper.updateCompletion(order);
		}
	}
}
