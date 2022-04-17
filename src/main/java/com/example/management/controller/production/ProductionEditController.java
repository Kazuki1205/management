package com.example.management.controller.production;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.ProductionForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.mapper.ProductionMapper;
import com.example.management.model.Item;
import com.example.management.service.ProductionService;
import com.example.management.validation.ValidOrder;

/**
 * 製作手配の編集コントローラー
 */
@Controller
public class ProductionEditController {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductionService productionService;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {

		return "製作手配(更新/削除)";
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
	 * 製作履歴一覧から選んだID情報を基に
	 * productionFormに詰め直して編集画面を表示する
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 製作手配テンプレート
	 */
	@GetMapping("/production/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		
		// ID情報を基にDBから製作手配テーブルを検索し製作手配クラスとして取得、製作手配フォームへ詰め替える。
		ProductionForm productionForm = modelMapper.map(productionMapper.findByIdExcludeInvalid(id), ProductionForm.class);
		
		model.addAttribute("productionForm", productionForm);
		
		return "productions/edit";
	}
	
	/**
	 * 製作手配情報を受け取り、DBを更新するメソッド
	 * 更新できたらリストにリダイレクト。
	 * 
	 * @param productionForm　製作手配フォーム
	 * @param bindingResult 製作手配フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * 
	 * @return　製作手配編集テンプレート/製作手配履歴へリダイレクト
	 */
	@PostMapping("/production/edit/update")
	public String update(@Validated(ValidOrder.class) @ModelAttribute("productionForm") ProductionForm productionForm, 
						 BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		
		// 製作手配フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");
			
			return "productions/edit";
		} else {
			
			// DBへINSERT処理、正常処理メッセージを表示。
			productionService.update(productionForm);
			
			redirectAttributes.addFlashAttribute("hasMessage", true);
			redirectAttributes.addFlashAttribute("class", "alert-info");
			redirectAttributes.addFlashAttribute("message", "登録に成功しました。");
				
			return "redirect:/production/log";
		}
	}
	
	
	@PostMapping("/production/edit/delete")
	public String delete(@ModelAttribute("productionForm") ProductionForm productionForm, 
						 RedirectAttributes redirectAttributes) {
		
		// DBへDELETE処理
		productionService.delete(productionForm);
		
		redirectAttributes.addFlashAttribute("hasMessage", true);
		redirectAttributes.addFlashAttribute("class", "alert-info");
		redirectAttributes.addFlashAttribute("message", "削除に成功しました。");
		
		return "redirect:/production/log";
	}
}
