package com.example.management.controller.customer;

import org.modelmapper.ModelMapper;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.CustomerForm;
import com.example.management.mapper.CustomerMapper;
import com.example.management.model.Customer;
import com.example.management.model.Employee;
import com.example.management.service.CommonService;
import com.example.management.service.CustomerService;
import com.example.management.validation.ValidOrder;

/**
 * 顧客マスタの編集コントローラー
 */
@Controller
public class CustomerEditController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private CustomerService customerService;
	
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
			
		return "顧客マスタ(更新/削除)";
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
	 * 顧客マスタ一覧から選んだID情報を基に
	 * customerFormに詰め直して編集画面を表示する
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 顧客編集テンプレート
	 */
	@GetMapping("/customer/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		
		// モデルマッパーの詰め替えから、setAddressの項目を除く。(住所項目が複数ヒットしてエラーが発生するため。)
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.typeMap(Customer.class, CustomerForm.class).addMappings(mapper -> mapper.skip(CustomerForm::setAddress));
		
		// ID情報を基にDBから顧客テーブルを検索し顧客クラスとして取得、顧客フォームへ詰め替える。
		CustomerForm customerForm = modelMapper.map(customerMapper.findByIdExcludeInvalid(id), CustomerForm.class);
		
		model.addAttribute("customerForm", customerForm);
		
		return "customers/edit";
	}
	
	/**
	 * 顧客情報を受け取り、DBを更新するメソッド
	 * 更新できたらリストにリダイレクト。
	 * 
	 * @param customerForm　顧客フォーム
	 * @param bindingResult 顧客フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * 
	 * @return　顧客編集テンプレート/顧客リストへリダイレクト
	 */
	@PostMapping("/customer/edit/update")
	public String update(@Validated(ValidOrder.class) @ModelAttribute("customerForm") CustomerForm customerForm, 
						 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		
		// 顧客フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "更新に失敗しました。"));
			
			return "customers/edit";
		} else {
			
			// DBへINSERT処理、正常処理メッセージを表示。
			customerService.update(customerForm);
			
			redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "更新に成功しました。"));
			
			return "redirect:/customer/list";
		}
	}
	
	/**
	 * 顧客情報をDBから削除するメソッド(論理削除)
	 * 削除が完了したらリストにリダイレクト。
	 * 
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param customerForm 顧客フォーム
	 * 
	 * @return　顧客マスタへリダイレクト
	 */
	@PostMapping("/customer/edit/delete")
	public String delete(@ModelAttribute("customerForm") CustomerForm customerForm, RedirectAttributes redirectAttributes) {
		
		// DBへDELETE処理
		customerService.delete(customerForm);
		
		redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "削除に成功しました。"));
		
		return "redirect:/customer/list";
	}

}
