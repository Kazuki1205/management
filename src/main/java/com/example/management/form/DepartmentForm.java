package com.example.management.form;

import lombok.Data;

/**
 * 部署マスタ登録画面の入力フォーム用クラス
 */
@Data
public class DepartmentForm {

	private Long id; // ID
	
	private String departmentCode; // 部署コード
	
	private String name; // 部署名
}
