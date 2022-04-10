package com.example.management.controller.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.ItemForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.model.Item;

/**
 * 商品マスタの一覧画面コントローラー
 */
@Controller
public class ItemListController {
	
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
		
		return "商品マスタ";
	}
	
	/**
	 * 商品マスタ一覧画面を表示
	 * 
	 * @param itemCode 検索条件に入力された商品コード
	 * @param name 検索条件に入力された商品名
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 社員マスタ一覧テンプレート
	 */
	@GetMapping("item/list")
	public String index(@ModelAttribute("itemForm") ItemForm itemForm, 
						@RequestParam(name = "itemCode", defaultValue = "") String itemCode, 
						@RequestParam(name = "name", defaultValue = "") String name, 
						Model model) {
		
		// 検索条件を基にDBから商品リストを取得する
		List<Item> items = itemMapper.findByConditions(itemCode, name);
		
		model.addAttribute("items", items);	
		model.addAttribute("itemForm", itemForm);
		
		return "items/list";
	}
	
	/**
	 * 商品詳細情報を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return　商品詳細テンプレート
	 */
	@GetMapping("/item/list/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// テンプレートへ渡す社員情報
		model.addAttribute("item", itemMapper.findById(id));
		
		return "items/detail";
	}
}
