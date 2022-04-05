package com.example.management.controller;

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
import com.example.management.repository.EmployeeRepository;
import com.example.management.service.registerEmployeeService;

/**
 * 社員マスタコントローラ
 */
@Controller
public class registerEmployeeController {

	@Autowired
	private registerEmployeeService registerEmployeeService;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * 社員マスタ登録画面を表示
	 * 
	 * @param model モデル
	 * @return 社員マスタテンプレート
	 */
	@GetMapping("/register/employee")
	public String index(Model model) {

		model.addAttribute("employeeForm", registerEmployeeService.getEmployeeForm());

		return "registers/employee";
	}

	/**
	 * 社員マスタ登録処理
	 * 
	 * @param employeeForm       社員フォーム
	 * @param resultEmployeeForm 社員フォームのバリデーション結果
	 * @param redirectAttributes リダイレクトアトリビュート
	 * @param model              モデル
	 * @return 社員マスタテンプレート
	 */
	@PostMapping("/register/employee/new")
	public String create(@Validated @ModelAttribute("employeeForm") EmployeeForm employeeForm,
			BindingResult resultEmployeeForm, RedirectAttributes redirectAttributes, Model model) {

		if (resultEmployeeForm.hasErrors()) {
			redirectAttributes.addFlashAttribute("hasMessage", true);
			redirectAttributes.addFlashAttribute("class", "alert-danger");
			redirectAttributes.addFlashAttribute("message", "登録に失敗しました。");

			return "redirect:/register/employee";
		}
		if (employeeRepository.findByUsername(employeeForm.getUsername()) == null) {
			registerEmployeeService.createEmployee(employeeForm);
		} else {
			redirectAttributes.addFlashAttribute("hasMessage", true);
			redirectAttributes.addFlashAttribute("class", "alert-danger");
			redirectAttributes.addFlashAttribute("message", "その社員IDはすでに使用されています。");
			
			return "redirect:/register/employee";
		}

		redirectAttributes.addFlashAttribute("hasMessage", true);
		redirectAttributes.addFlashAttribute("class", "alert-info");
		redirectAttributes.addFlashAttribute("message", "登録に成功しました。");

		return "redirect:/register/employee";
	}
}
