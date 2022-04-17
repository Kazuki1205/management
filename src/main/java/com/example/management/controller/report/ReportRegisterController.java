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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.ReportForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.model.Employee;
import com.example.management.model.Production;
import com.example.management.service.ReportService;

/**
 * 日報入力の新規登録コントローラー
 */
@Controller
public class ReportRegisterController {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ReportService reportService;
	
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
	 * @return 製作手配型のリスト
	 */
	@ModelAttribute(name = "productions")
	public List<Production> getProductions() {
		
		List<Production> productions = productionMapper.findAllExcludeInvalid();
		
		return productions;
	}
	
	/**
	 * 日報入力新規登録画面を表示
	 * 
	 * @param reportForm 日報入力フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報入力新規登録テンプレート
	 */
	@GetMapping("/report/register")
	public String register(@ModelAttribute("reportForm") ReportForm reportForm, Model model) {
		
		model.addAttribute("reportForm", reportForm);
		
		return "reports/register";
	}
	
	/**
	 * 日報入力新規登録メソッド
	 * 
	 * @param reportForm 日報入力フォーム
	 * @param bindingResult 日報入力フォームのバリデーション結果
	 * @param employee セキュリティ認証されている社員情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報入力新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/report/register/new")
	public String create(@Validated @ModelAttribute("reportForm") ReportForm reportForm, 
						 BindingResult bindingResult, 
						 @AuthenticationPrincipal Employee employee, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 日報入力フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");	
		} else {
		
			// 製作数以上の完了数・不良数が入力された場合、エラーメッセージを表示。使用されていなければDBへINSERT処理、正常処理メッセージを表示。
			if (reportForm.getCompletionQuantity() + reportForm.getFailureQuantity() <= productionMapper.getLotQuantity(reportForm.getProductionId())) {
				
				// 日報入力フォームと社員IDでDBへinsert処理
				reportService.create(reportForm, employee.getId());
				
				// フォーム再送を防ぐ為、リダイレクト。
				redirectAttributes.addFlashAttribute("hasMessage", true);
				redirectAttributes.addFlashAttribute("class", "alert-info");
				redirectAttributes.addFlashAttribute("message", "登録に成功しました。");
				
				return "redirect:/report/register";
			} else {
				
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "完了数・不良数の値が不正です。製作数以下にして下さい。");	
			}
		}
		
		model.addAttribute("reportForm", reportForm);
		
		return "reports/register";
	}
	
	/**
	 * 日報入力画面でセレクトボックスの製作番号の値を変更した際に、
	 * Jqueryのajaxにより呼び出されるメソッド。
	 * 受け取った製作手配IDを基に、商品履歴テーブルの商品名を返す。
	 * 
	 * @param id 製作手配ID
	 * 
	 * @return production 製作手配クラス(Json形式で返す)
	 */
	@PostMapping("/report/register/ajax")
	@ResponseBody
	public Production ajax(@RequestParam Long id) {
		
		Production production = productionMapper.findById(id);
		
		return production;
	}
}
