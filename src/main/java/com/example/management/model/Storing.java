package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 入庫クラス
 */
@Data
public class Storing {

	private Long id; // ID
	
	private Production production; // 製作クラス
	
	private Integer storingQuantity; // 入庫数
	
	private Integer storingQuantityTotal; // 入庫テーブルを製作IDでグループ化した際の入庫数集計結果
	
	private Integer failureQuantityTotal; // 日報テーブルを製作IDでグループ化した際の不良数集計結果
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
}
