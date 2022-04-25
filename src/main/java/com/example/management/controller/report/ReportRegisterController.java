package com.example.management.controller.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.ReportForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.model.Employee;
import com.example.management.model.Production;
import com.example.management.service.CommonService;
import com.example.management.service.ReportService;

/**
 * 日報の新規登録コントローラー
 */
@Controller
public class ReportRegisterController {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ReportService reportService;
	
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
		
		return "日報入力(新規登録)";
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 社員マスタ登録画面のセレクトボックスの部署一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 製作型のリスト
	 */
	@ModelAttribute(name = "productions")
	public List<Production> getProductions() {
		
		List<Production> productions = productionMapper.findAllExcludeInvalidAndCompletion();
		
		return productions;
	}
	
	/**
	 * 日報新規登録画面を表示
	 * 
	 * @param reportForm 日報フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報新規登録テンプレート
	 */
	@GetMapping("/report/register")
	public String register(@ModelAttribute("reportForm") ReportForm reportForm, Model model) {
		
		model.addAttribute("reportForm", reportForm);
		
		return "reports/register";
	}
	
	/**
	 * 日報新規登録メソッド
	 * 
	 * @param reportForm 日報フォーム
	 * @param bindingResult 日報フォームのバリデーション結果
	 * @param employee セキュリティ認証されている社員情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/report/register/new")
	public String create(@Validated @ModelAttribute("reportForm") ReportForm reportForm, 
						 BindingResult bindingResult, 
						 @AuthenticationPrincipal Employee employee, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		// 日報フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
		} else {
			
			// 日報フォームと社員IDでDBへinsert処理
			reportService.create(reportForm, employee.getId());
			
			// フォーム再送を防ぐ為、リダイレクト。
			redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
			
			return "redirect:/report/register";		
		}
		
		model.addAttribute("reportForm", reportForm);
		
		return "reports/register";
	}
}
