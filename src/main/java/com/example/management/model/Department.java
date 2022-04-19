package com.example.management.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * 部署クラス
 */
@Data
public class Department {
	
	private Long id; // ID
	
	private String code; // 部署コード
	
	private String name; // 部署名
	
	private List<Employee> employees; // 所属社員
	
	private LocalDateTime createdAt; // 作成日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ
}
