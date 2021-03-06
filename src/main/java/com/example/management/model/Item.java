package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 商品クラス
 */
@Data
public class Item {
	
	private Long id; // ID
	
	private String code; // 商品コード
	
	private String name; // 品名
	
	private Long unitPrice; // 単価
	
	private LocalDateTime createdAt; // 作成日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ
}
