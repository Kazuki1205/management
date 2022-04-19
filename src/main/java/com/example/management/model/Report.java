package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 日報クラス
 */
@Data
public class Report {

	private Long id; // ID
	
	private Production production; // 製作クラス
	
	private EmployeeHistory employeeHistory; // 社員履歴クラス
	
	private Integer completionQuantity; // 完了数
	
	private Integer failureQuantity; // 不良数
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ
}
