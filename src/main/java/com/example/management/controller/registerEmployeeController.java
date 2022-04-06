package com.example.management.controller;

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
import com.example.management.service.registerEmployeeService;
import com.example.management.validation.ValidOrder;

/**
 * 社員マスタコントローラ
 */
@Controller
public class registerEmployeeController {

	@Autowired
	private registerEmployeeService registerEmployeeService;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 社員マスタ登録画面のセレクトボックスの部署一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 部署型のリスト
	 */
	@ModelAttribute
	public List<Department> getDepartmentList() {
		
		List<Department> departmentList = departmentMapper.findAll();
		
		return departmentList;
	}
	
	/**
	 * 社員マスタ登録画面を表示
	 * 
	 * @param employeeForm 社員フォーム ※@ModelAttributeにより、無ければnewされる。
	 * @param model テンプレートから受け取る情報
	 * 
	 * @return 社員マスタテンプレート
	 */
	@GetMapping("/register/employee")
	public String index(@ModelAttribute("employeeForm") EmployeeForm employeeForm, Model model) {
		
		// 社員フォームの社員IDに「0001」形式のString型をセットする
		employeeForm.setUsername(registerEmployeeService.getEmployeeUsername());
		model.addAttribute("employeeForm", employeeForm);
		
		return "registers/employee";
	}

	/**
	 * 社員マスタ登録処理
	 * 
	 * @param employeeForm 社員フォーム
	 * @param resultEmployeeForm 社員フォームのバリデーション結果
	 * @param model テンプレートから受け取る情報
	 * 
	 * @return 社員マスタテンプレート
	 */
	@PostMapping("/register/employee/new")
	public String create(@Validated(ValidOrder.class) @ModelAttribute("employeeForm") EmployeeForm employeeForm, 
			BindingResult resultEmployeeForm, Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 社員フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (resultEmployeeForm.hasErrors()) {
			model.addAttribute("message", "登録に失敗しました。");
			model.addAttribute("class", "alert-danger");

		} else {
			
			// 社員IDが既に使用されている場合、エラーメッセージを表示。使用されていければDBへINSERT処理、正常処理メッセージを表示。
			if (employeeMapper.findByUsername(employeeForm.getUsername()) == null) {
				registerEmployeeService.createEmployee(employeeForm);
				
				model.addAttribute("message", "登録に成功しました。");
				model.addAttribute("class", "alert-info");
			} else {
				model.addAttribute("message", "その社員IDはすでに使用されています。");
				model.addAttribute("class", "alert-danger");
			}
		}
		
		// 社員フォームの社員IDに「0001」形式のString型をセットする
		employeeForm.setUsername(registerEmployeeService.getEmployeeUsername());
		model.addAttribute("employeeForm", employeeForm);
		return "registers/employee";
	}
}
