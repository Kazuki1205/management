package com.example.management.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.EmployeeForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.EmployeeMapper;
import com.example.management.model.Department;
import com.example.management.model.Employee;

/**
 * 社員マスタの一覧画面コントローラー
 */
@Controller
public class EmployeeListController {
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "社員マスタ";
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
	 * 社員マスタ一覧画面を表示
	 * 
	 * @param username 検索条件に入力された社員ID
	 * @param name 検索条件に入力された社員名
	 * @param departmentId 検索条件に入力された部署ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 社員マスタ一覧テンプレート
	 */
	@GetMapping("employee/list")
	public String index(@ModelAttribute("employeeForm") EmployeeForm employeeForm, 
						@RequestParam(name = "username", defaultValue = "") String username, 
						@RequestParam(name = "name", defaultValue = "") String name, 
						@RequestParam(name = "departmentId", defaultValue = "") Long departmentId, 
						Model model) {
		
		// 検索条件を基にDBから社員リストを取得する
		List<Employee> employees = employeeMapper.findByConditions(username, name, departmentId);
		
		model.addAttribute("employees", employees);
		model.addAttribute("employeeForm", employeeForm);
		
		return "employees/list";
	}
	
	/**
	 * 社員詳細情報を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return　社員詳細テンプレート
	 */
	@GetMapping("/employee/list/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// テンプレートへ渡す社員情報
		model.addAttribute("employee", employeeMapper.findById(id));
		
		return "employees/detail";
	}
}








