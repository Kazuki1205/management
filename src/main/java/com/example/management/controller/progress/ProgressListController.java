package com.example.management.controller.progress;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.ProgressForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.model.Production;
import com.example.management.service.ProductionService;

/**
 * 進捗一覧コントローラー
 */
@Controller
public class ProgressListController {
	
	@Autowired
	private ProductionMapper productionMapper;
	
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
		
		return "進捗一覧";
	}

	/**
	 * 製作中の進捗一覧を表示する。
	 * 検索フォームに入力された項目で、製作テーブルをあいまい検索する。
	 * 
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param progressForm 進捗フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 進捗一覧テンプレート
	 */
	@GetMapping("/progress/list")
	public String index(@RequestParam(name = "lotNumber", defaultValue = "") String lotNumber, 
						@RequestParam(name = "itemCode", defaultValue = "") String itemCode, 
						@RequestParam(name = "itemName", defaultValue = "") String itemName, 
						@ModelAttribute("progressForm") ProgressForm progressForm, 
						Model model) {
		
		// 製作テーブルの削除・完了済み以外のレコードを取得。
		List<Production> productions = productionMapper.findByConditionsExcludeInvalidAndCompletion(lotNumber, itemCode, itemName);
		
		// 製作クラス内の日報クラスリストを、テンプレート表示用に整形して渡す。
		model.addAttribute("productions", productionService.adjustProductions(productions));
		
		System.out.println("テスト" + model.getAttribute("productions"));
		
		return "progresses/list";
	}
}
