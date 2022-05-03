package com.example.management.controller.item;

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

import com.example.management.form.ItemForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.model.Employee;
import com.example.management.service.CommonService;
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
		itemForm.setCode(itemService.getCode());
		
		model.addAttribute("itemForm", itemForm);
		
		return "items/register";
	}
	
	/**
	 * 商品マスタ新規登録メソッド
	 * 
	 * @param itemForm 商品フォーム
	 * @param bindingResult 商品フォームのバリデーション結果
	 * @param redirectAttirbutes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 商品新規登録テンプレート/リダイレクト
	 */
	@PostMapping("/item/register/new")
	public String create(@Validated @ModelAttribute("itemForm") ItemForm itemForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		// 商品フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {

			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
		} else {
			
			// 商品コードが既に使用されている場合、エラーメッセージを表示。使用されていなければDBへINSERT処理、正常処理メッセージを表示。
			if (itemMapper.findByCode(itemForm.getCode()) == null) {
				
				itemService.create(itemForm);
				
				// フォーム再送を防ぐ為、リダイレクト。
				redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
				
				return "redirect:/item/register";
			} else {
				
				model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "その商品コードはすでに使用されています。"));
			}
		}
		
		// 商品フォームの商品コードに「00000001」形式のString型をセットする
		itemForm.setCode(itemService.getCode());
		
		model.addAttribute("itemForm", itemForm);
		
		return "items/register";
	}

}
