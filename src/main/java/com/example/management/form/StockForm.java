package com.example.management.form;

import lombok.Data;

/**
 * 在庫画面の検索入力フォーム用クラス
 */
@Data
public class StockForm {
	
	private String itemCode; // 商品コード
	
	private String itemName; // 商品名
}
