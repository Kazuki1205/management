package com.example.management.controller.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.OrderDetailForm;
import com.example.management.form.OrderForm;
import com.example.management.mapper.CustomerMapper;
import com.example.management.mapper.ItemMapper;
import com.example.management.mapper.OrderMapper;
import com.example.management.model.Customer;
import com.example.management.model.Employee;
import com.example.management.model.Item;
import com.example.management.service.CommonService;
import com.example.management.service.OrderService;

/**
 * 受注の新規登録コントローラー
 */
@Controller
public class OrderRegisterController {
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "受注入力(新規登録)";
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
	 * 受注登録画面を表示
	 * 
	 * @param employee 社員クラス
	 * @param orderForm 受注フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注登録テンプレート
	 */
	@GetMapping("/order/register")
	public String register(@AuthenticationPrincipal Employee employee, 
						   @ModelAttribute("orderForm") OrderForm orderForm, 
						   Model model) {
		
		// フォームに最新の受注番号をセットする。
		orderForm.setOrderNumber(orderService.getOrderNumber());
		
		// 空のリスト<受注明細フォーム>を作成する。
		List<OrderDetailForm> list = new ArrayList<>();
		
		// 作成したリストに空の受注明細フォームをaddする。
		list.add(new OrderDetailForm());
		
		// 受注フォームにList<OrderDetailForm>(1件の空フォーム)をセットする。
		orderForm.setOrderDetailForms(list);
		
		model.addAttribute("orderForm", orderForm);
		
		return "orders/register";
	}
	
	/**
	 * 受注明細入力の「追加」ボタンを押した際、
	 * orderFormの中のList<orderDetailForm>受注明細フォームのリストに
	 * 新しいフォームをaddし、セットして返すメソッド。
	 * 
	 * @param orderForm 受注フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注登録テンプレート
	 */
	@PostMapping(value = "/order/register", params = "add")
	public String addList(@ModelAttribute("orderForm") OrderForm orderForm, 
						  Model model) {
		
		// 受注フォームの受注明細フォームリストに新規受注明細フォームをaddする。
		orderForm.getOrderDetailForms().add(new OrderDetailForm());
		
		model.addAttribute("orderForm", orderForm);
		
		return "orders/register";
	}
	
	/**
	 * 受注明細入力の「削除」ボタンを押した際、
	 * orderFormの中のList<orderDetailForm>受注明細フォームのリストから
	 * 受け取ったインデックス番号を基に、removeする。
	 * 
	 * @param orderForm 受注フォーム
	 * @param index フォーム画面のインデックス番号
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注登録テンプレート
	 */
	@PostMapping(value = "/order/register", params = "remove")
	public String removeList(@ModelAttribute("orderForm") OrderForm orderForm, 
							 @RequestParam("remove") int index, 
							 Model model) {
		
		// 受注フォームの明細リストを変数に代入する。
		List<OrderDetailForm> orderDetailForms = orderForm.getOrderDetailForms();
		
		// 受注明細フォームのリストのサイズが1より多い場合のみ、インデックスを基に削除処理を行う。
		if (orderDetailForms.size() > 1) {
			
			orderDetailForms.remove(index);
			
			// 受注明細フォームをセットし直す。
			orderForm.setOrderDetailForms(orderDetailForms);
		}
		
		model.addAttribute("orderForm", orderForm);
		
		return "orders/register";
	}
	
	/**
	 * 受注フォームに入力された情報を基に、
	 * 受注テーブル・受注明細テーブルへinsert処理を行うメソッド。
	 * 
	 * @param orderForm 受注フォーム
	 * @param bindingResult 受注・受注明細フォームのバリデーション結果
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注登録テンプレート
	 */
	@PostMapping("/order/register")
	public String create(@Validated @ModelAttribute("orderForm") OrderForm orderForm, 
						 BindingResult bindingResult, 
						 @AuthenticationPrincipal Employee employee, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		// 受注フォームにバリデーションエラーがあった場合、エラーメッセージを表示。受注登録テンプレートを返す。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
			
			return "orders/register";
		} else {
			
			// 受注フォームの受注番号が既に受注テーブルに存在した場合、エラーメッセージを表示。無ければDBへinsert処理。
			if (orderMapper.findByOrderNumber(orderForm.getOrderNumber()) == null) {
				
				// 受注・受注明細テーブルへinsert処理
				orderService.create(orderForm, employee.getId());
				
				redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
				
				return "redirect:/order/register";		
			} else {
				
				model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "その受注番号はすでに使用されています。"));
				
				return "orders/register";
			}
		}	
	}
}
