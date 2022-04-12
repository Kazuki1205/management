package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 商品クラス
 */
@Data
public class Item {
	
	public Item () {} // コンストラクタ(MyBatisで使用)
	
	private Long id; // ID
	
	private String code; // 商品コード
	
	private String name; // 品名
	
	private Long unitPrice; // 単価
	
	private Long carryOverStock; //　繰越在庫
	
	private LocalDateTime createdAt; // 作成日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Integer invalid; // 削除フラグ
}
