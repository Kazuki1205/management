package com.example.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * 製作クラス
 */
@Data
public class Production {

	private Long id; // ID
	
	private List<Report> reports; // 日報クラスのリスト
	
	private Item item; // 商品クラス
	
	private String lotNumber; // 製作番号
	
	private Integer lotQuantity; // 製作数
	
	private LocalDate scheduledCompletionDate; // 完成予定日
	
	private LocalDate completionDate; // 完成日
	
	private Integer departmentCompletionQuantityTotal; // 入力者の所属する部署毎の、該当製番に対する完了数計
	
	private Integer failureQuantityTotal; // 該当製番に対する不良数計
	
	private Integer storingQuantityTotal; // 該当製番に対する入庫数計
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ
}
