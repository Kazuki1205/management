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
import com.example.management.model.Production;
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
		
		model.addAttribute("hasMessage", true);
		
		// 入庫フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");
		} else {
			
			// 製作テーブルから入庫フォームの製作IDを基に、1件レコードを取得する。
			Production production = productionMapper.findById(storingForm.getProductionId());
			
			// 入庫フォームで入力された製作IDを基に、製作テーブルの情報を取得し、製作完了区分が「0」であれば新規登録。「1」であれば既に完了済みのため、エラーメッセージを表示。
			if (production.getCompletionFlag() == 0) {
				
				storingService.create(storingForm, production);
				
				redirectAttributes.addFlashAttribute("hasMessage", true);
				redirectAttributes.addFlashAttribute("class", "alert-info");
				redirectAttributes.addFlashAttribute("message", "登録に成功しました。");
				
				return "redirect:/storing/register";
			} else {
				
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "登録に失敗しました。");
			}
		}
		
		model.addAttribute("storingForm", storingForm);
		
		return "storings/register";
	}
}
