package com.example.management.controller.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.DepartmentForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.service.CommonService;
import com.example.management.service.DepartmentService;

/**
 * 部署マスタの新規登録コントローラー
 */
@Controller
public class DepartmentRegisterController {
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
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

		return "部署マスタ(新規登録)";
	}
	
	/**
	 * 部署マスタ登録画面を表示
	 * 
	 * @param departmentForm 部署フォーム ※@ModelAttributeにより、無ければnewされる。
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 部署新規登録テンプレート
	 */
	@GetMapping("/department/register")
	public String register(@ModelAttribute("departmentForm") DepartmentForm departmentForm, Model model) {
		
		// 部署フォームの部署コードに「0001」形式のString型をセットする
		departmentForm.setCode(departmentService.getCode());
		
		model.addAttribute("departmentForm", departmentForm);
		
		return "departments/register";
	}
	
	/**
	 * 部署マスタ新規登録メソッド
	 * 
	 * @param departmentForm 部署フォーム
	 * @param bindingResult 部署フォームのバリデーション結果
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 部署マスタ新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/department/register/new")
	public String create(@Validated @ModelAttribute("departmentForm") DepartmentForm departmentForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		// 部署フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {

			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
		} else {
			
			// 部署コードが既に使用されている場合、エラーメッセージを表示。使用されていなければDBへINSERT処理、正常処理メッセージを表示。
			if (departmentMapper.findByCode(departmentForm.getCode()) == null) {
				
				departmentService.create(departmentForm);
				
				// フォーム再送を防ぐ為、リダイレクト。
				redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
				
				return "redirect:/department/register";
			} else {
				
				model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "その部署コードはすでに使用されています。"));
			}
		}
		
		// 部署フォームの部署コードに「0001」形式のString型をセットする
		departmentForm.setCode(departmentService.getCode());
		
		model.addAttribute("departmentForm", departmentForm);
		
		return "departments/register";
	}
}
