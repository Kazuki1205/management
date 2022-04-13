package com.example.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 製作手配クラス
 */
@Data
public class Production {

	private Long id; // ID
	
	private Item item; // 商品クラス
	
	// 出荷完了クラスのList型を予定
	
	private String lotNumber; // 製作番号
	
	private Integer lotQuantity; // 製作数
	
	private LocalDate scheduledCompletionDate; // 完成予定日
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ
}
