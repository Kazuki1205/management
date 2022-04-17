package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 商品履歴クラス
 */
@Data
public class ItemHistory {
	
	private Long id; // ID
	
	private Long itemId; // 商品ID
	
	private String code; // 商品コード
	
	private String name; // 商品名
	
	private Long unitPrice; // 単価
	
	private LocalDateTime updatedAt; // 更新日時
}
