package com.example.management.form;

import lombok.Data;

/**
 * 進捗一覧画面の検索フォームクラス
 */
@Data
public class ProgressForm {

	private String lotNumber; // 製作番号
	
	private String itemCode; // 商品コード
	
	private String itemName; // 商品名
}
