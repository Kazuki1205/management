package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 社員履歴クラス
 */
@Data
public class EmployeeHistory {
	
	private Long id; // ID
	
	private Long employeeId; // ID(社員テーブルの主キー)
	
	private String username; // 社員ID
	
	private String name; // 社員名
	
	private Department department; // 部署クラス
	
	private LocalDateTime updatedAt; // 更新日時

}
