package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 在庫クラス
 */
@Data
public class Stock {

	private Long id; // ID
	
	private Long itemId; // 商品ID
	
	private Integer actualQuantity; // 実在庫
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
}
