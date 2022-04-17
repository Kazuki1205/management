package com.example.management.controller.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.ReportForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.model.Department;
import com.example.management.model.Report;

/**
 * 日報入力の履歴画面コントローラー
 */
@Controller
public class ReportLogController {
	
	@Autowired
	private ReportMapper reportMapper;
	
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
		
		return "日報履歴";
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
	 * 日報履歴一覧を表示
	 * 
	 * @param reportForm 日報入力フォーム
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param departmentId 部署ID
	 * @param employeeName 社員名
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報入力詳細テンプレート
	 */
	@GetMapping("/report/log")
	public String index(@ModelAttribute("reportForm") ReportForm reportForm, 
						@RequestParam(name = "lotNumber", defaultValue = "") String lotNumber, 
						@RequestParam(name = "itemCode", defaultValue = "") String itemCode, 
						@RequestParam(name = "itemName", defaultValue = "") String itemName, 
						@RequestParam(name = "departmentId", defaultValue = "") Long departmentId, 
						@RequestParam(name = "employeeName", defaultValue = "") String employeeName, 
						Model model) {
		
		// 検索条件を基にDBから日報入力リストを取得する
		List<Report> reports = reportMapper.findByConditions(lotNumber, itemCode, itemName, departmentId, employeeName);
		
		model.addAttribute("reports", reports);
		model.addAttribute("reportForm", reportForm);
		
		return "reports/log";
	}
	
	/**
	 * 日報履歴詳細情報を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報入力詳細テンプレート
	 */
	@GetMapping("/report/log/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// テンプレートへ渡す日報入力情報
		model.addAttribute("report", reportMapper.findById(id));
		
		return "reports/detail";
	}
}
