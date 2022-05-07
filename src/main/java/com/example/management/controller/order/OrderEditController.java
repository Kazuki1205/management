package com.example.management.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.OrderDetailForm;
import com.example.management.form.OrderForm;
import com.example.management.mapper.CustomerMapper;
import com.example.management.mapper.ItemMapper;
import com.example.management.mapper.OrderDetailMapper;
import com.example.management.mapper.OrderMapper;
import com.example.management.mapper.ShippingMapper;
import com.example.management.model.Customer;
import com.example.management.model.Employee;
import com.example.management.model.Item;
import com.example.management.model.Order;
import com.example.management.service.CommonService;
import com.example.management.service.OrderService;

/**
 * 受注の編集画面コントローラー
 */
@Controller
public class OrderEditController {
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShippingMapper shippingMapper;

	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "受注入力(更新/削除)";
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 社員マスタ登録画面のセレクトボックスの部署一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return customers 受注型のリスト
	 */
	@ModelAttribute(name = "customers")
	public List<Customer> getCustomers() {
		
		List<Customer> customers = customerMapper.findAllExcludeInvalid();
		
		return customers;
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 製作登録画面のセレクトボックスの商品一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return items 商品型のリスト
	 */
	@ModelAttribute(name = "items")
	public List<Item> getItems() {
		
		List<Item> items = itemMapper.findAllExcludeInvalid();
		
		return items;
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 入力フォームを値に置き換える。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 編集フラグ
	 */
	@ModelAttribute(name = "editFlag")
	public Integer setEditFlag() {
		
		Integer editFlag = 1;
		
		return editFlag;
	}
	
	/**
	 * 受注編集画面を表示するメソッド。
	 * 担当者以外は編集不可の為、受注履歴にリダイレクト。
	 * 
	 * @param employee 社員クラス
	 * @param orderId 受注ID
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model　テンプレート先へ渡す情報
	 * 
	 * @return 受注編集テンプレート/受注履歴へリダイレクト
	 */
	@GetMapping("/order/edit/{id}")
	public String edit(@AuthenticationPrincipal Employee employee, 
					   @PathVariable("id") Long orderId, 
					   RedirectAttributes redirectAttributes, Model model) {
		
		// 受注テーブルからパス変数の受注IDを基に情報を取得する。
		Order order = orderMapper.findByIdExcludeInvalidAndCompletion(orderId);
		
		// 編集画面に遷移しようとした社員IDと、受注入力時の社員IDが異なる場合、編集不可とし受注履歴画面へリダイレクト。
		if (employee.getId() == order.getEmployeeHistory().getEmployeeId()) {
			
			// 受注フォームに必要な情報を、受注クラスから全てマッピングしテンプレートへ渡す。
			model.addAttribute("orderForm", orderService.formMapping(order));
			
			return "orders/edit";
		} else {
			
			redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-danger", "受注入力している担当者のみ編集可能です。"));
			
			return "redirect:/order/log";
		}
	}
	
	/**
	 * 受注明細入力行の「削除」ボタンを押した際、
	 * orderFormの中のList<orderDetailForm>受注明細フォームのリストから
	 * 「更新」を押した際にDBから削除する明細行を指定する。
	 * 
	 * @param orderForm 受注フォーム
	 * @param index フォーム画面のインデックス番号
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注登録テンプレート
	 */
	@PostMapping(value = "/order/edit", params = "remove")
	public String removeList(@ModelAttribute("orderForm") OrderForm orderForm, 
							 @RequestParam("remove") int index, 
							 Model model) {
		
		// 明細行情報を取得
		OrderDetailForm orderDetailForm = orderForm.getOrderDetailForms().get(index);
		
		// 削除ボタンを押した明細行を削除予定(更新を押した段階でDBから削除)とする。
		orderDetailForm.setInvalid((short)1);
		
		// 受注明細テーブルの受注数を取得する。
		Integer orderQuantity = orderDetailMapper.findByPrimaryKey(orderForm.getOrderNumber(), orderDetailForm.getDetailId()).getOrderQuantity();
		
		// フォームにDBの受注数量をセットし直す。
		orderDetailForm.setOrderQuantity(orderQuantity);
		
		// 受注小計を計算する。
		Long orderAmount = orderDetailForm.getUnitPrice() * orderQuantity;
		
		// 受注小計をセット。
		orderDetailForm.setOrderAmount(orderAmount);

		model.addAttribute("orderForm", orderForm);
		
		return "orders/edit";
	}
	
	/**
	 * 受注編集画面の「更新」ボタンが押された際、
	 * その時点の受注明細フォームの入力状況を、各テーブルに更新する。
	 * 
	 * @param orderForm 受注フォーム
	 * @param bindingResult 各フォームのバリデーション結果
 	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注編集テンプレート/受注履歴へリダイレクト
	 */
	@PostMapping("/order/edit/update")
	public String update(@Validated @ModelAttribute("orderForm") OrderForm orderForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		// 入力フォームでバリデーションエラーが発生した場合、エラーメッセージを表示しテンプレートを返す。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
		} else {
			
			// 受注明細フォームを取り出す。
			List<OrderDetailForm> orderDetailForms = orderForm.getOrderDetailForms();
			
			// エラーフラグを用意。
			Short errorFlag = 0;
			
			// for内で受注明細フォームを回す。「出荷済計 > 受注数」の様に、出荷数が上回る場合はエラーメッセージを表示。
			for (OrderDetailForm orderDetailForm : orderDetailForms) {
				
				// 受注数を取得。
				Integer orderQuantity = orderDetailForm.getOrderQuantity();
				
				// 出荷済計を取得。
				Integer shippingQuantityTotal = shippingMapper.sumOfShippingQuantityTotal(orderForm.getOrderNumber(), orderDetailForm.getDetailId()).orElse(0);
				
				// 「出荷済計 > 受注数」に該当した場合エラーフラグを立てる。
				if (shippingQuantityTotal > orderQuantity) {
					
					errorFlag = 1;
				}
			}
			
			// ↑のfor内でエラーが発生しなかった場合に、DBへupdate処理。
			if (errorFlag != 1) {
			
				// DBへupdate処理
				orderService.update(orderForm);
				
				redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "更新に成功しました。"));
				
				return "redirect:/order/log";
			} else {
				
				model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "既に出荷済の数量未満の受注数が入力されています。"));
			}
		}
		
		return "orders/edit";
	}
	
	/**
	 * 受注編集画面自体の「削除」ボタンが押された際に、
	 * 受注テーブル・対応する受注明細テーブルの削除フラグを立てる。
	 * 
	 * @param orderForm 受注フォーム
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * 
	 * @return 受注履歴へリダイレクト
	 */
	@PostMapping("order/edit/delete")
	public String delete(@ModelAttribute("orderForm") OrderForm orderForm, RedirectAttributes redirectAttributes) {
		
		// フォームで受け取った受注IDを受注クラスにセットする。
		Order order = orderMapper.findByIdExcludeInvalidAndCompletion(orderForm.getId());
		
		// DBへの削除(論理)処理
		orderService.delete(order);
		
		redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "削除に成功しました。"));
		
		return "redirect:/order/log";
	}
}
