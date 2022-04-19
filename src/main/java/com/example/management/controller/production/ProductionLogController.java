package com.example.management.controller.production;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.ProductionForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.model.Production;

/**
 * 製作の履歴画面コントローラー
 */
@Controller
public class ProductionLogController {
	
	@Autowired
	private ProductionMapper productionMapper;

	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "製作履歴";
	}
	
	/**
	 * 製作履歴一覧を表示
	 * 
	 * @param productionForm 製作フォーム
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 製作履歴テンプレート
	 */
	@GetMapping("/production/log")
	public String index(@ModelAttribute("productionForm") ProductionForm productionForm, 
						@RequestParam(name = "lotNumber", defaultValue = "") String lotNumber, 
						@RequestParam(name = "itemCode", defaultValue = "") String itemCode, 
						@RequestParam(name = "itemName", defaultValue = "") String itemName, 
						Model model) {
		
		// 検索条件を基にDBから製作リストを取得する
		List<Production> productions = productionMapper.findByConditions(lotNumber, itemCode, itemName);
		
		model.addAttribute("productions", productions);
		model.addAttribute("productionForm", productionForm);
		
		return "productions/log";
	}
	
	/**
	 * 製作詳細情報を表示
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return　製作詳細テンプレート
	 */
	@GetMapping("/production/log/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// テンプレートへ渡す社員情報
		model.addAttribute("production", productionMapper.findById(id));
		
		return "productions/detail";
	}
}
