package com.example.management.controller.report;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.ReportForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.model.Employee;
import com.example.management.model.Production;
import com.example.management.model.Report;
import com.example.management.service.ReportService;

/**
 * 日報入力の編集コントローラー
 */
@Controller
public class ReportEditController {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ReportMapper reportMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
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

		return "日報入力(更新/削除)";
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
	 * 日報履歴一覧から選んだID情報を基に
	 * reportFormに詰め直して編集画面を表示する
	 * 
	 * @param employee セキュリティ認証されている社員情報
	 * @param id ID
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報入力画面テンプレート/日報履歴画面テンプレート
	 */
	@GetMapping("/report/edit/{id}")
	public String edit(@AuthenticationPrincipal Employee employee, 
					   @PathVariable("id") Long id, 
					   RedirectAttributes redirectAttributes, Model model) {
		
			// URLから取得したIDを基に、日報入力テーブルから該当レコードを取得する。
			Report report = reportMapper.findById(id);
			
			// 該当レコードを登録した社員IDではない社員が編集ボタンを押した場合、履歴テンプレートへリダイレクト
			if (employee.getId() == report.getEmployeeHistory().getEmployeeId()) {
	
				// 日報入力クラスから日報入力フォームへ詰め替える。
				ReportForm reportForm = modelMapper.map(report, ReportForm.class);
				
				// 取得した日報入力クラスから、商品名・製作数を取得しセットする。
				reportForm.setItemName(report.getProduction().getItem().getName());
				reportForm.setLotQuantity(report.getProduction().getLotQuantity());
				
				model.addAttribute("reportForm", reportForm);
				
				return "reports/edit";	
			} else {
				
				redirectAttributes.addFlashAttribute("hasMessage", true);
				redirectAttributes.addFlashAttribute("class", "alert-danger");
				redirectAttributes.addFlashAttribute("message", "日報入力している担当者のみ編集可能です。");
				
				return "redirect:/report/log";
			}
	}
	
	/**
	 * 日報入力更新メソッド
	 * 
	 * @param reportForm 日報入力フォーム
	 * @param bindingResult 日報入力フォームのバリデーション結果
	 * @param employee セキュリティ認証されている社員情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 日報入力新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/report/edit/update")
	public String update(@Validated @ModelAttribute("reportForm") ReportForm reportForm, BindingResult bindingResult, 
						 @AuthenticationPrincipal Employee employee, RedirectAttributes redirectAttributes, 
						 Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 日報入力フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");
			
			return "reports/edit";	
		} else {
			
			// 製作数以上の完了数・不良数が入力された場合、エラーメッセージを表示。
			if (reportForm.getCompletionQuantity() + reportForm.getFailureQuantity() <= productionMapper.getLotQuantity(reportForm.getProductionId())) {
				
				// DBへINSERT処理、正常処理メッセージを表示。
				reportService.update(reportForm, employee.getId());
				
				redirectAttributes.addFlashAttribute("hasMessage", true);
				redirectAttributes.addFlashAttribute("class", "alert-info");
				redirectAttributes.addFlashAttribute("message", "登録に成功しました。");	
				
				return "redirect:/report/log";
			} else {

				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "完了数・不良数の値が不正です。製作数以下にして下さい。");
				
				return "reports/edit";
				}
		
		}
	}
	
	/**
	 * 
	 * 
	 * @param employee セキュリティ認証されている社員情報
	 * @param reportForm
	 * @param redirectAttributes
	 * 
	 * @return
	 */
	@PostMapping("/report/edit/delete")
	public String delete(@ModelAttribute("reportForm") ReportForm reportForm, RedirectAttributes redirectAttributes) {
		
		// DBへDELETE処理
		reportService.delete(reportForm);
		
		redirectAttributes.addFlashAttribute("hasMessage", true);
		redirectAttributes.addFlashAttribute("class", "alert-info");
		redirectAttributes.addFlashAttribute("message", "削除に成功しました。");
		
		return "redirect:/report/log";
	}
}
