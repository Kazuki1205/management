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

import com.example.management.form.EmployeeForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.EmployeeMapper;
import com.example.management.model.Department;
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
	 * @return 部署型のリスト
	 */
	@ModelAttribute(name = "departmentList")
	public List<Department> getDepartmentList() {
		
		List<Department> departmentList = departmentMapper.findAll();
		
		return departmentList;
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
		employeeForm.setUsername(employeeService.getEmployeeUsername());
		
		model.addAttribute("employeeForm", employeeForm);
		
		return "employees/register";
	}

	/**
	 * 社員マスタ登録処理
	 * 
	 * @param employeeForm 社員フォーム
	 * @param bindingResult 社員フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 社員新規登録テンプレート
	 */
	@PostMapping("/employee/register/new")
	public String create(@Validated(ValidOrder.class) @ModelAttribute("employeeForm") EmployeeForm employeeForm, 
			BindingResult bindingResult, Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 社員フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");

		} else {
			
			// 社員IDが既に使用されている場合、エラーメッセージを表示。使用されていければDBへINSERT処理、正常処理メッセージを表示。
			if (employeeMapper.findByUsernameExcludeInvalid(employeeForm.getUsername()) == null) {
				
				employeeService.createEmployee(employeeForm);
				
				employeeForm = new EmployeeForm();
				
				model.addAttribute("class", "alert-info");
				model.addAttribute("message", "登録に成功しました。");	
			} else {
				
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "その社員IDはすでに使用されています。");
			}
		}
		
		// 社員フォームの社員IDに「0001」形式のString型をセットする
		employeeForm.setUsername(employeeService.getEmployeeUsername());
		
		model.addAttribute("employeeForm", employeeForm);
		
		return "employees/register";
	}
}
