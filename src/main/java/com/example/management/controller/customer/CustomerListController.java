package com.example.management.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.CustomerForm;
import com.example.management.mapper.CustomerMapper;
import com.example.management.model.Customer;

/**
 * 顧客マスタの一覧画面コントローラー
 */
@Controller
public class CustomerListController {
	
	@Autowired
	private CustomerMapper customerMapper;

	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "顧客マスタ";
	}
	
	/**
	 * 顧客マスタ一覧画面を表示
	 * 
	 * @param customerForm 顧客フォーム
	 * @param name 顧客名
	 * @param address 住所
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 顧客マスタ一覧テンプレート
	 */
	@GetMapping("/customer/list")
	public String index(@ModelAttribute("customerForm") CustomerForm customerForm, 
						@RequestParam(name = "customerCode", defaultValue = "") String customerCode, 
						@RequestParam(name = "name", defaultValue = "") String name, 
						@RequestParam(name = "address", defaultValue = "") String address, 
						Model model) {
		
		// 検索条件を基にDBから顧客リストを取得する
		List<Customer> customers = customerMapper.findByConditions(customerCode, name, address);
		
		model.addAttribute("customers", customers);
		model.addAttribute("customerForm", customerForm);
		
		return "customers/list";
	}
	
	/**
	 * 顧客詳細情報を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return　顧客詳細テンプレート
	 */
	@GetMapping("/customer/list/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// テンプレートへ渡す顧客情報
		model.addAttribute("customer", customerMapper.findById(id));
		
		return "customers/detail";
	}
}
