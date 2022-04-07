package com.example.management.controller.employee;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.EmployeeForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.EmployeeMapper;
import com.example.management.model.Department;
import com.example.management.service.EmployeeService;
import com.example.management.validation.ValidOrder;

/**
 * 社員マスタの編集コントローラー
 */
@Controller
public class EmployeeEditController {
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
			
		return "社員マスタ(編集/削除)";
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
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 社員情報編集の際、パスワード・パスワード確認用を非表示にするフラグを立てる。
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
	 * 社員マスタ一覧から選んだID情報を基に
	 * employeeFormに詰め直して編集画面を表示する
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 社員編集テンプレート
	 */
	@GetMapping("/employee/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		
		EmployeeForm employeeForm = modelMapper.map(employeeMapper.findById(id), EmployeeForm.class);
		
		model.addAttribute("employeeForm", employeeForm);
		
		return "employees/edit";
	}
	
	/**
	 * 社員情報を受け取り、DBを更新するメソッド
	 * 
	 * @param employeeForm　社員フォーム
	 * @param bindingResult 社員フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return　社員編集テンプレート
	 */
	@PostMapping("/employee/edit/update")
	public String update(@Validated(ValidOrder.class) @ModelAttribute("employeeForm") EmployeeForm employeeForm, 
						 BindingResult bindingResult, Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 社員フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasFieldErrors("name") || bindingResult.hasFieldErrors("departmentId")) {
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "更新に失敗しました。");
		} else {
			
			// DBへINSERT処理、正常処理メッセージを表示。
			employeeService.updateEmployee(employeeForm);
			
			model.addAttribute("class", "alert-info");
			model.addAttribute("message", "更新に成功しました。");
		}
		
		return "employees/edit";
	}
	
	/**
	 * 社員情報をDBから削除するメソッド(論理削除)
	 * 
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param employeeForm 社員フォーム
	 * 
	 * @return　社員マスタへリダイレクト
	 */
	@PostMapping("/employee/edit/delete")
	public String delete(RedirectAttributes redirectAttributes, EmployeeForm employeeForm) {
		
		// DBへDELETE処理
		employeeService.deleteEmployee(employeeForm);
		
		redirectAttributes.addFlashAttribute("hasMessage", true);
		redirectAttributes.addFlashAttribute("class", "alert-info");
		redirectAttributes.addFlashAttribute("message", "削除に成功しました。");
		
		System.out.println("テスト" + employeeForm);
		
		return "redirect:/employee/list";
	}
}
