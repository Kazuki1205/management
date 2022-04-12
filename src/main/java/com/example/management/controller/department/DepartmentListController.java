package com.example.management.controller.department;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.DepartmentForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.model.Department;

/**
 * 部署マスタの一覧画面コントローラー
 */
@Controller
public class DepartmentListController {
	
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
		
		return "部署マスタ";
	}
	
	/**
	 * 部署マスタ一覧画面を表示
	 * 
	 * @param departmentForm 部署フォーム
	 * @param name 部署名
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 部署マスタ一覧テンプレート
	 */
	@GetMapping("/department/list")
	public String index(@ModelAttribute("departmentForm") DepartmentForm departmentForm, 
						@RequestParam(name = "code", defaultValue = "") String code, 
						@RequestParam(name = "name", defaultValue = "") String name, 
						Model model) {
		
		// 検索条件を基にDBから部署リストを取得する
		List<Department> departments = departmentMapper.findByConditions(code, name);
		
		model.addAttribute("departments", departments);
		model.addAttribute("departmentForm", departmentForm);
		
		return "departments/list";
	}
	
	/**
	 * 部署詳細情報を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return　部署詳細テンプレート
	 */
	@GetMapping("/department/list/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// テンプレートへ渡す部署情報
		model.addAttribute("department", departmentMapper.findById(id));
		
		return "departments/detail";
	}
}
