package com.example.management.controller.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.StockForm;
import com.example.management.mapper.StockMapper;
import com.example.management.model.Stock;

/**
 * 在庫の一覧画面コントローラー
 */
@Controller
public class StockListController {
	
	@Autowired
	private StockMapper stockMapper;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "在庫一覧";
	}

	/**
	 * 在庫一覧画面を表示する。
	 * 
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param stockForm 在庫フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 在庫一覧テンプレート
	 */
	@GetMapping("/stock/list")
	public String index(@RequestParam(name = "itemCode", defaultValue = "") String itemCode, 
						@RequestParam(name = "itemName", defaultValue = "") String itemName, 
						@ModelAttribute("stockForm") StockForm stockForm, Model model) {
		
		
		// 検索フォームに入力された商品コードと商品名を基に、在庫テーブルをあいまい検索する。
		List<Stock> stocks = stockMapper.findByConditions(itemCode, itemName);
		
		model.addAttribute("stocks", stocks);
		
		return "stocks/list";
	}
	
	/**
	 * 在庫詳細画面を表示
	 * 
	 * @param id 在庫ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 在庫詳細テンプレート
	 */
	@GetMapping("/stock/list/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// パス変数の在庫IDを基に、在庫テーブルの該当レコードを取得し、テンプレートへ渡す。
		model.addAttribute("stock", stockMapper.findById(id));
		
		return "stocks/detail";
	}
}
