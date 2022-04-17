package com.example.management.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.CustomerForm;
import com.example.management.mapper.CustomerMapper;
import com.example.management.service.CustomerService;
import com.example.management.validation.ValidOrder;

/**
 * 顧客マスタの新規登録コントローラー
 */
@Controller
public class CustomerRegisterController {
	
	@Autowired
	private CustomerService customerService;
	
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

		return "顧客マスタ(新規登録)";
	}
	
	/**
	 * 顧客マスタ登録画面を表示
	 * 
	 * @param itemForm 顧客フォーム ※@ModelAttributeにより、無ければnewされる。
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 顧客新規登録テンプレート
	 */
	@GetMapping("/customer/register")
	public String register(@ModelAttribute("customerForm") CustomerForm customerForm, Model model) {
		
		// 顧客フォームの顧客コードに「0001」形式のString型をセットする
		customerForm.setCode(customerService.getCode());
		model.addAttribute("customerForm", customerForm);
		
		return "customers/register";
	}
	
	/**
	 * 顧客マスタ新規登録メソッド
	 * 
	 * @param customerForm 顧客フォーム
	 * @param bindingResult 顧客フォームのバリデーション結果
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model　テンプレートへ渡す情報
	 * 
	 * @return 顧客マスタ新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/customer/register/new")
	public String create(@Validated(ValidOrder.class) @ModelAttribute("customerForm") CustomerForm customerForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 顧客フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
				
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");
		} else {
			
			// 顧客コードが既に使用されている場合、エラーメッセージを表示。使用されていなければDBへINSERT処理、正常処理メッセージを表示。
			if (customerMapper.findByCode(customerForm.getCode()) == null) {
			
			customerService.create(customerForm);
			
			// フォーム再送を防ぐ為、リダイレクト。
			redirectAttributes.addFlashAttribute("hasMessage", true);
			redirectAttributes.addFlashAttribute("class", "alert-info");
			redirectAttributes.addFlashAttribute("message", "登録に成功しました。");	
			
			return "redirect:/customer/register";
			} else {
				
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "その顧客コードはすでに使用されています。");
			}
		}
		
		// 顧客フォームの顧客コードに「0001」形式のString型をセットする
		customerForm.setCode(customerService.getCode());
		
		model.addAttribute("customerForm", customerForm);
		
		return "customers/register";
	}
}
