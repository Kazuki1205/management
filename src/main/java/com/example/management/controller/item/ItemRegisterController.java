package com.example.management.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.management.form.ItemForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.service.ItemService;
import com.example.management.validation.ValidOrder;

/**
 * 商品マスタの新規登録コントローラー
 */
@Controller
public class ItemRegisterController {
	
	@Autowired
	private ItemService itemService;
	
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

		return "商品マスタ(新規登録)";
	}
	
	/**
	 * 商品マスタ登録画面を表示
	 * 
	 * @param itemForm 商品フォーム ※@ModelAttributeにより、無ければnewされる。
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 商品新規登録テンプレート
	 */
	@GetMapping("/item/register")
	public String register(@ModelAttribute("itemForm") ItemForm itemForm, Model model) {
		
		// 商品フォームの商品コードに「00000001」形式のString型をセットする
		itemForm.setItemCode(itemService.getItemCode());
		
		model.addAttribute("itemForm", itemForm);
		
		return "items/register";
	}
	
	
	@PostMapping("/item/register/new")
	public String create(@Validated(ValidOrder.class) @ModelAttribute("itemForm") ItemForm itemForm, 
						 BindingResult bindingResult, 
						 Model model) {
		
		model.addAttribute("hasMessage", true);
		
		// 商品フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {

			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "登録に失敗しました。");
		} else {
			
			// 商品コードが既に使用されている場合、エラーメッセージを表示。使用されていければDBへINSERT処理、正常処理メッセージを表示。
			if (itemMapper.findByItemCode(itemForm.getItemCode()) == null) {
				
				itemService.createItem(itemForm);
				
				model.addAttribute("class", "alert-info");
				model.addAttribute("message", "登録に成功しました。");
			} else {
				
				model.addAttribute("class", "alert-danger");
				model.addAttribute("message", "その商品コードはすでに使用されています。");
			}
			
			// 商品フォームの商品コードに「00000001」形式のString型をセットする
			itemForm.setItemCode(itemService.getItemCode());
			
			model.addAttribute("itemForm", itemForm);
		}
		
		return "items/register";
	}

}
