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
import com.example.management.service.CommonService;
import com.example.management.service.ProductionService;
import com.example.management.validation.ValidOrder;

/**
 * 製作の編集画面コントローラー
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

		return "製作手配(更新/削除)";
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 製作登録画面のセレクトボックスの商品一覧を取得する。
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
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 入力フォームを値に置き換える。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 編集フラグ
	 */
	@ModelAttribute(name = "editFlag")
	public Integer setEditFlag() {
		
		Integer editFlag = 1;
		
		return editFlag;
	}
	
	/**
	 * 製作履歴一覧から選んだID情報を基に
	 * productionFormに詰め直して編集画面を表示する
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 製作テンプレート
	 */
	@GetMapping("/production/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		
		// ID情報を基にDBから製作テーブルを検索し製作クラスとして取得、製作フォームへ詰め替える。
		ProductionForm productionForm = modelMapper.map(productionMapper.findByIdExcludeInvalidAndCompletion(id), ProductionForm.class);
		
		model.addAttribute("productionForm", productionForm);
		
		return "productions/edit";
	}
	
	/**
	 * 製作情報を受け取り、DBを更新するメソッド
	 * 更新できたらリストにリダイレクト。
	 * 
	 * @param productionForm　製作フォーム
	 * @param bindingResult 製作フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * 
	 * @return　製作編集テンプレート/製作履歴へリダイレクト
	 */
	@PostMapping("/production/edit/update")
	public String update(@Validated(ValidOrder.class) @ModelAttribute("productionForm") ProductionForm productionForm, 
						 BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		
		// 製作フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
			
			return "productions/edit";
		} else {
			
			// DBへINSERT処理、正常処理メッセージを表示。
			productionService.update(productionForm);
			
			redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
				
			return "redirect:/production/log";
		}
	}
	
	/**
	 * 製作情報をDBから削除するメソッド(論理削除)
	 * 削除が完了したらリストにリダイレクト。
	 * 
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param productionForm 製作フォーム
	 * 
	 * @return　製作履歴へリダイレクト
	 */
	@PostMapping("/production/edit/delete")
	public String delete(@ModelAttribute("productionForm") ProductionForm productionForm, 
						 RedirectAttributes redirectAttributes) {
		
		// DBへDELETE処理
		productionService.delete(productionForm);
		
		redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "削除に成功しました。"));
		
		return "redirect:/production/log";
	}
}
