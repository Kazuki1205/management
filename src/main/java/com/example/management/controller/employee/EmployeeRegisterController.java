package com.example.management.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.EmployeeForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.EmployeeMapper;
import com.example.management.model.Department;
import com.example.management.service.CommonService;
import com.example.management.service.EmployeeService;
import com.example.management.validation.ValidOrder;

/**
 * 社員マスタの新規登録コントローラー
 */
@Controller
public class EmployeeRegisterController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
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

		return "社員マスタ(新規登録)";
	}
	
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 社員マスタ登録画面のセレクトボックスの部署一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return departments 部署型のリスト
	 */
	@ModelAttribute(name = "departments")
	public List<Department> getDepartments() {
		
		List<Department> departments = departmentMapper.findAllExcludeInvalid();
		
		return departments;
	}
	
	/**
	 * 社員マスタ登録画面を表示
	 * 
	 * @param employeeForm 社員フォーム ※@ModelAttributeにより、無ければnewされる。
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 社員新規登録テンプレート
	 */
	@GetMapping("/employee/register")
	public String index(@ModelAttribute("employeeForm") EmployeeForm employeeForm, Model model) {
		
		// 社員フォームの社員IDに「0001」形式のString型をセットする
		employeeForm.setUsername(employeeService.getUsername());
		
		model.addAttribute("employeeForm", employeeForm);
		
		return "employees/register";
	}

	/**
	 * 社員マスタ登録処理
	 * 
	 * @param employeeForm 社員フォーム
	 * @param bindingResult 社員フォームのバリデーション結果
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 社員新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/employee/register/new")
	public String create(@Validated(ValidOrder.class) @ModelAttribute("employeeForm") EmployeeForm employeeForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		// 社員フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));

		} else {
			
			// 社員IDが既に使用されている場合、エラーメッセージを表示。使用されていなければDBへINSERT処理、正常処理メッセージを表示。
			if (employeeMapper.findByUsername(employeeForm.getUsername()) == null) {
				
				employeeService.create(employeeForm);
				
				// フォーム再送を防ぐ為、リダイレクト。
				redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
				
				return "redirect:/employee/register";
			} else {
				
				model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "その社員IDはすでに使用されています。"));
			}
		}
		
		// 社員フォームの社員IDに「0001」形式のString型をセットする
		employeeForm.setUsername(employeeService.getUsername());
		
		model.addAttribute("employeeForm", employeeForm);
		
		return "employees/register";
	}
}
