package com.example.management.controller.storing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.StoringForm;
import com.example.management.mapper.StoringMapper;
import com.example.management.model.Storing;

/**
 * 日報の履歴画面コントローラー
 */
@Controller
public class StoringLogController {
	
	@Autowired
	private StoringMapper storingMapper;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {

		return "入庫履歴";
	}

	/**
	 * 入庫履歴一覧を表示
	 * 
	 * @param storingForm 入庫フォーム
	 * @param lotNumber　製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 入庫履歴テンプレート
	 */
	@GetMapping("/storing/log")
	public String index(@ModelAttribute("storingForm") StoringForm storingForm, 
						@RequestParam(name = "lotNumber", defaultValue = "") String lotNumber, 
						@RequestParam(name = "itemCode", defaultValue = "") String itemCode, 
						@RequestParam(name = "itemName", defaultValue = "") String itemName, 
						Model model) {
		
		// 検索条件を基にDBから入庫リストを取得する
		List<Storing> storings = storingMapper.findByConditions(lotNumber, itemCode, itemName);
		
		model.addAttribute("storings", storings);
		model.addAttribute("storingForm", storingForm);
		
		return "storings/log";
	}
	
	/**
	 * 入庫情報詳細画面を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 入庫情報詳細テンプレート
	 */
	@GetMapping("/storing/log/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {

		// URLのIDを基に入庫テーブルから情報を取得する。
		model.addAttribute("storing", storingMapper.findById(id));
		
		return "storings/detail";
	}
}
