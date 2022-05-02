package com.example.management.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.example.management.validation.ValidGroup1;
import com.example.management.validation.ValidGroup2;

import lombok.Data;

/**
 * 商品マスタ登録画面の入力フォーム用クラス
 */
@Data
public class ItemForm {

	private Long id; // ID
	
	private String code; // 商品コード
	
	@Pattern(regexp = "^[^\\p{javaWhitespace}]+", message = "空白文字は使用できません。")
	@Size(min = 1, max = 32)
	private String name; // 品名	
	
	@NotNull(message="入力して下さい。")
	@PositiveOrZero(message = "「0」以上の数値を設定して下さい。")
	private Long unitPrice; // 単価
}
