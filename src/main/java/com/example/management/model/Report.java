package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 日報クラス
 */
@Data
public class Report {

	private Long id; // ID
	
	private Long productionId; // 製作ID
	
	private Long departmentId; // 部署ID
	
	private Production production; // 製作クラス
	
	private EmployeeHistory employeeHistory; // 社員履歴クラス
	
	private Integer completionQuantity; // 完了数
	
	private Integer failureQuantity; // 不良数
	
	private Integer departmentCompletionQuantityTotal; // 部署毎の、該当製番に対する完了数計
	
	private Integer departmentFailureQuantityTotal; // 部署毎の、該当製番に対する不良数計
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private LocalDateTime updatedAtLatest; // 最終更新日時
	
	private Short invalid; // 削除フラグ
}
