package com.example.management.controller.production;

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

import com.example.management.form.ProductionForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.mapper.ProductionMapper;
import com.example.management.model.Item;
import com.example.management.service.ProductionService;
import com.example.management.validation.ValidOrder;

/**
 * 製作手配の新規登録コントローラー
 */
@Controller
public class ProductionRegisterController {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ProductionService productionService;
	
	@Autowired
	private ItemMapper itemMapper;

	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {

		return "製作手配(新規登録)";
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 製作手配登録画面のセレクトボックスの商品一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return items 商品型のリスト
	 */
	@ModelAttribute(name = "items")
	public List<Item> getItems() {
		
		List<Item> items = itemMapper.findAllExcludeInvalid();
		
		return items;
	}
	
	/**
	 * 製作手配登録画面を表示
	 * 
	 * @param productionForm 製作手配フォーム ※@ModelAttributeにより、無ければnewされる。
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 製作手配新規登録テンプレート
	 */
	@GetMapping("/production/register")
	public String register(@ModelAttribute("productionForm") ProductionForm productionForm, Model model) {
		
		// 製作手配フォームの製作番号に「2201-01-0001」形式のString型をセットする
		productionForm.setLotNumber(productionService.getLotNumber());
		
		model.addAttribute("productionForm", productionForm);
		
		return "productions/register";
	}
	
	/**
	 * 製作手配新規登録メソッド
	 * 
	 * @param productionForm 製作手配フォーム
	 * @param bindingResult 製作手配フォームのバリデーション結果
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 製作手配新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/production/register/new")
	public String create(@Validated(ValidOrder.class) @ModelAttribute("productionForm") ProductionForm productionForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 製作手配フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");
		} else {
			
			// 製作番号が既に使用されている場合、エラーメッセージを表示。使用されていなければDBへINSERT処理、正常処理メッセージを表示。
			if (productionMapper.findByLotNumber(productionForm.getLotNumber()) == null) {
				
				productionService.create(productionForm);
				
				// フォーム再送を防ぐ為、リダイレクト。
				redirectAttributes.addFlashAttribute("hasMessage", true);
				redirectAttributes.addFlashAttribute("class", "alert-info");
				redirectAttributes.addFlashAttribute("message", "登録に成功しました。");	
				
				return "redirect:/production/register";
			} else {
				
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "その製作番号はすでに使用されています。");
			}

		}
		
		// 製作手配フォームの製作番号に「2201-01-0001」形式のString型をセットする
		productionForm.setLotNumber(productionService.getLotNumber());
			
		model.addAttribute("productionForm", productionForm);
		
		return "productions/register";
	}
}
