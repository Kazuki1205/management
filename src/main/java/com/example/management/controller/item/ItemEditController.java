package com.example.management.controller.item;

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

import com.example.management.form.ItemForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.service.CommonService;
import com.example.management.service.ItemService;
import com.example.management.validation.ValidOrder;

/**
 * 商品マスタの編集コントローラー
 */
@Controller
public class ItemEditController {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ItemService itemService;
	
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
			
		return "商品マスタ(更新/削除)";
	}

	/**
	 * 商品マスタ一覧から選んだID情報を基に
	 * itemFormに詰め直して編集画面を表示する
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 商品編集テンプレート
	 */
	@GetMapping("/item/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		
		// ID情報を基にDBから商品テーブルを検索し商品クラスとして取得、商品フォームへ詰め替える。
		ItemForm itemForm = modelMapper.map(itemMapper.findByIdExcludeInvalid(id), ItemForm.class);
		
		model.addAttribute("itemForm", itemForm);
		
		return "items/edit";
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
	 * 商品情報を受け取り、DBを更新するメソッド
	 * 更新できたらリストにリダイレクト。
	 * 
	 * @param itemForm　商品フォーム
	 * @param bindingResult 商品フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * 
	 * @return　商品編集テンプレート/商品リストへリダイレクト
	 */
	@PostMapping("/item/edit/update")
	public String update(@Validated @ModelAttribute("itemForm") ItemForm itemForm, 
						 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		
		// 商品フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "更新に失敗しました。"));
			
			return "items/edit";
		} else {
			
			// DBへINSERT処理、正常処理メッセージを表示。
			itemService.update(itemForm);
			
			redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "更新に成功しました。"));
			
			return "redirect:/item/list";
		}
	}
	
	/**
	 * 商品情報をDBから削除するメソッド(論理削除)
	 * 削除が完了したらリストにリダイレクト。
	 * 
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param itemForm 商品フォーム
	 * 
	 * @return　商品マスタへリダイレクト
	 */
	@PostMapping("/item/edit/delete")
	public String delete(@ModelAttribute("itemForm") ItemForm itemForm, RedirectAttributes redirectAttributes) {
		
		// DBへDELETE処理
		itemService.delete(itemForm);
		
		redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "削除に成功しました。"));
		
		return "redirect:/item/list";
	}
}
