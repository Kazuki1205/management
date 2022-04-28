package com.example.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.management.mapper.ItemMapper;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.mapper.StoringMapper;
import com.example.management.model.Employee;
import com.example.management.model.Item;
import com.example.management.model.Production;
import com.example.management.service.CommonService;

/**
 * 各フォームの値を非同期で書き換えるコントローラー
 * 戻り値はすべてJson形式で返す。
 */
@RestController
public class AjaxController {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ReportMapper reportMapper;
	
	@Autowired
	private StoringMapper storingMapper;
	
	@Autowired
	private ItemMapper itemMapper;

	/**
	 * Jqueryのajaxにより呼び出されるメソッド。
	 * 受け取った製作IDを基に、各情報を返す。
	 * 
	 * @param productionId 製作ID
	 * 
	 * @return production 製作クラス(Json形式で返す)
	 */
	@PostMapping("/ajax/production")
	public Production ajaxProduction(@AuthenticationPrincipal Employee employee, @RequestParam Long productionId) {
		
		// 製作IDを基に、製作テーブルの1件のレコードを取得する。
		Production production = productionMapper.findByIdExcludeInvalid(productionId);
		
		// 日報テーブルの完了数計(製作IDと部署IDでグループ化したものの集計)を取得する。nullなら0とする。
		production.setDepartmentCompletionQuantityTotal(reportMapper.sumOfCompletionQuantity(productionId, employee.getDepartment().getId()).orElse(0));
		
		// 日報テーブルの不良数計(製作IDでグループ化したものの集計)を取得する。nullなら0とする。
		production.setFailureQuantityTotal(reportMapper.sumOfFailureQuantity(productionId).orElse(0));
		
		// 入庫テーブルの入庫数計(製作IDでグループ化したものの集計)を取得する。nullなら0とする。
		production.setStoringQuantityTotal(storingMapper.sumOfStoringQuantity(productionId).orElse(0));
		
		return production;
	}
	
	/**
	 * Jqueryのajaxにより呼び出されるメソッド。
	 * 受け取った商品IDを基に、各情報を返す。
	 * 
	 * @param itemId 商品ID
	 * 
	 * @return Item 商品クラス(Json形式で返す)
	 */
	@PostMapping("/ajax/item")
	public Item ajaxItem(@RequestParam Long itemId) {
		
		// フォームに入力された商品IDを基に、商品テーブルの商品情報を取得する。
		Item item = itemMapper.findByIdExcludeInvalid(itemId);
		
		return item;
	}
}
