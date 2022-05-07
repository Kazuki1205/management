package com.example.management.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.OrderForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.OrderMapper;
import com.example.management.model.Department;
import com.example.management.model.Order;

/**
 * 受注の履歴画面コントローラー
 */
@Controller
public class OrderLogController {
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "受注履歴";
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 社員マスタ登録画面のセレクトボックスの部署一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 部署型のリスト
	 */
	@ModelAttribute(name = "departments")
	public List<Department> getDepartments() {
		
		List<Department> departments = departmentMapper.findAllExcludeInvalid();
		
		return departments;
	}
	
	/**
	 * 受注履歴一覧を表示
	 * 
	 * @param orderForm 受注フォーム
	 * @param orderNumber 受注番号
	 * @param customerCode 顧客コード
	 * @param customerName 顧客名
	 * @param departmentId 部署ID
	 * @param employeeName 社員名
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注履歴テンプレート
	 */
	@GetMapping("/order/log")
	public String index(@ModelAttribute("orderForm") OrderForm orderForm, 
						@RequestParam(name = "orderNumber", defaultValue = "") String orderNumber, 
						@RequestParam(name = "customerCode", defaultValue = "") String customerCode, 
						@RequestParam(name = "customerName", defaultValue = "") String customerName, 
						@RequestParam(name = "departmentId", defaultValue = "") Long departmentId, 
						@RequestParam(name = "employeeName", defaultValue = "") String employeeName, 
						Model model) {
		
		// フォームに入力された情報を基に、受注テーブルから該当レコードの情報を取得する。
		List<Order> orders = orderMapper.findByConditions(orderNumber, customerCode, customerName, departmentId, employeeName);
		
		model.addAttribute("orders", orders);
		model.addAttribute("orderForm", orderForm);	
		
		return "orders/log";
	}
	
	/**
	 * 受注詳細画面を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 受注詳細テンプレート
	 */
	@GetMapping("/order/log/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// URLから取得したIDを基に受注テーブルから該当レコードの情報を取得。
		model.addAttribute("order", orderMapper.findById(id));
		
		return "orders/detail";
	}
}
