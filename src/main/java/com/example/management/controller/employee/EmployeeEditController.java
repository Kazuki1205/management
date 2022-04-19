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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.EmployeeForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.EmployeeMapper;
import com.example.management.model.Department;
import com.example.management.model.Employee;
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
			
		return "社員マスタ(更新/削除)";
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
		
		// ID情報を基にDBから社員テーブルを検索し社員クラスとして取得、社員フォームへ詰め替える。
		EmployeeForm employeeForm = modelMapper.map(employeeMapper.findByIdExcludeInvalid(id), EmployeeForm.class);
		
		model.addAttribute("employeeForm", employeeForm);
		
		return "employees/edit";
	}
	
	/**
	 * 社員情報を受け取り、DBを更新するメソッド
	 * 更新できたらリストにリダイレクト。
	 * 
	 * @param employeeForm　社員フォーム
	 * @param bindingResult 社員フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * 
	 * @return　社員編集テンプレート/社員リストへリダイレクト
	 */
	@PostMapping("/employee/edit/update")
	public String update(@Validated(ValidOrder.class) @ModelAttribute("employeeForm") EmployeeForm employeeForm, 
						 BindingResult bindingResult, Model model, 
						 RedirectAttributes redirectAttributes) {
		
		// 社員フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasFieldErrors("name") || bindingResult.hasFieldErrors("departmentId")) {
			
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "更新に失敗しました。");
			
			return "employees/edit";
		} else {
			
			// DBへINSERT処理、正常処理メッセージを表示。
			employeeService.update(employeeForm);
			
			redirectAttributes.addFlashAttribute("hasMessage", true);
			redirectAttributes.addFlashAttribute("class", "alert-info");
			redirectAttributes.addFlashAttribute("message", "更新に成功しました。");
			
			return "redirect:/employee/list";
		}
	}
	
	/**
	 * 社員情報をDBから削除するメソッド(論理削除)
	 * 削除が完了したらリストにリダイレクト。
	 * 
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param employeeForm 社員フォーム
	 * 
	 * @return　社員マスタへリダイレクト
	 */
	@PostMapping("/employee/edit/delete")
	public String delete(RedirectAttributes redirectAttributes, EmployeeForm employeeForm) {
		
		// DBへDELETE処理
		employeeService.delete(employeeForm);
		
		redirectAttributes.addFlashAttribute("hasMessage", true);
		redirectAttributes.addFlashAttribute("class", "alert-info");
		redirectAttributes.addFlashAttribute("message", "削除に成功しました。");
		
		return "redirect:/employee/list";
	}
	
	/**
	 * 社員情報編集画面から
	 * パスワードを変更ボタンを押した際に呼ばれるメソッド
	 * 
	 * @param id　ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return パスワード変更テンプレート
	 */
	@PostMapping("/employee/edit/password")
	public String editPassword(@RequestParam("id") Long id, Model model) {
		
		// ID情報を基にDBから社員テーブルを検索し社員クラスとして取得、社員フォームへ詰め替える。
		Employee employee = employeeMapper.findById(id);
		EmployeeForm employeeForm = modelMapper.map(employee, EmployeeForm.class);
		
		model.addAttribute("employee", employee);
		model.addAttribute("employeeForm", employeeForm);
		
		// employees/common.htmlのパスワード登録フォームのみ表示。
		model.addAttribute("editFlag", null);
		model.addAttribute("changePasswordFlag", 1);
		
		return "employees/changePassword";
	}
	
	/**
	 * パスワード編集画面から変更ボタンを押した際に呼ばれるメソッド
	 * 変更できた場合にリストへリダイレクト。
	 * 
	 * @param employeeForm　社員フォーム
	 * @param bindingResult　社員フォームのバリデーション結果
	 * @param id　ID
	 * @param model テンプレートへ渡す情報
	 * @param redirectAttributes　リダイレクト先へ渡す情報
	 * 
	 * @return　パスワード変更テンプレート/社員リストへリダイレクト
	 */
	@PostMapping("/employee/edit/password/change")
	public String changePassword(
			@Validated(ValidOrder.class) @ModelAttribute("employeeForm") EmployeeForm employeeForm, 
			BindingResult bindingResult, @RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		
		// パスワードとパスワード確認用がバリデーションエラーになった場合、再度テンプレートを返す。
		if (bindingResult.hasFieldErrors("password") || bindingResult.hasFieldErrors("passwordValid")) {
			
			// 画面に出す社員情報を取得。
			Employee employee = employeeMapper.findById(id);
			model.addAttribute("employee", employee);
			
			// employees/common.htmlのパスワード登録フォームのみ表示。
			model.addAttribute("editFlag", null);
			model.addAttribute("changePasswordFlag", 1);
			
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "更新に失敗しました。");
			
			return "employees/changePassword";
		} else {
			
			// フォームで受け取ったパスワードをエンコードしてDBへ更新。
			employeeService.updatePassword(employeeForm);
			
			redirectAttributes.addFlashAttribute("hasMessage", true);
			redirectAttributes.addFlashAttribute("class", "alert-info");
			redirectAttributes.addFlashAttribute("message", "更新に成功しました。");
			
			return "redirect:/employee/list";
		}
		
	}
	
}
