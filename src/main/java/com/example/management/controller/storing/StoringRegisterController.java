package com.example.management.controller.storing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.StoringForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.mapper.StoringMapper;
import com.example.management.model.Production;
import com.example.management.service.CommonService;
import com.example.management.service.StoringService;

/**
 * 入庫の新規登録コントローラー
 */
@Controller
public class StoringRegisterController {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private StoringService storingService;
	
	@Autowired
	private ReportMapper reportMapper;
	
	@Autowired
	private StoringMapper storingMapper;
	
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
	 * 入庫新規登録画面を表示
	 * 
	 * @param storingForm 入庫フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 入庫新規登録テンプレート
	 */
	@GetMapping("/storing/register")
	public String register(@ModelAttribute("storingForm") StoringForm storingForm, Model model) {
		
		model.addAttribute("storingForm", storingForm);
		
		return "storings/register";
	}
	
	/**
	 * 
	 * 
	 * @param storingForm 入庫フォーム
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 入庫新規登録テンプレート/入庫履歴へリダイレクト
	 */
	@PostMapping("/storing/register/new")
	public String create(@Validated @ModelAttribute("storingForm") StoringForm storingForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, Model model) {
		
		// 入庫フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
		} else {
				
				// フォームで選択された製作IDを基に、製作テーブルの情報を取得する。
				Production production = productionMapper.findByIdExcludeInvalidAndCompletion(storingForm.getProductionId());
				
				// 製作テーブルの製作数を取得する。
				Integer lotQuantity = production.getLotQuantity();
				
				// 日報テーブルの不良数計(製作IDでグループ化したものの集計)を取得する。nullなら0とする。
				Integer failureQuantityTotal = reportMapper.sumOfFailureQuantity(production.getId()).orElse(0);
				
				// 入庫テーブルの入庫数計(製作IDでグループ化したものの集計)を取得する。nullなら0とする。
				Integer storingQuantityTotal = storingMapper.sumOfStoringQuantity(production.getId()).orElse(0);
				
				// 「製作数 - 不良数 - 入庫数計」より、フォームに入力された入庫数が上回った場合、エラーメッセージを表示。以内であれば入庫テーブルに挿入処理。
				if (lotQuantity - failureQuantityTotal - storingQuantityTotal >= storingForm.getStoringQuantity()) {
					
					// 入庫テーブルに挿入処理。
					storingService.create(storingForm, production, lotQuantity, failureQuantityTotal, storingQuantityTotal);
				
					redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
				
				return "redirect:/storing/register";
				} else {
				
				model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "仕掛数より多い入庫数が入力されています。"));
			}
		}
		
		model.addAttribute("storingForm", storingForm);
		
		return "storings/register";
	}
}
