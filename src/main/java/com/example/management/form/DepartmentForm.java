package com.example.management.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 部署マスタ登録画面の入力フォーム用クラス
 */
@Data
public class DepartmentForm {

	private Long id; // ID
	
	private String code; // 部署コード
	
	@Pattern(regexp = "^[^\\p{javaWhitespace}]+", message = "空白文字は使用できません。")
	@Size(min = 1, max = 32)
	private String name; // 部署名
}
