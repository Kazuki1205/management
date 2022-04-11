package com.example.management.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.management.validation.ValidGroup1;

import lombok.Data;

/**
 * 顧客マスタ登録画面の入力フォーム用クラス
 */
@Data
public class CustomerForm {

	private Long id; // ID
	
	private String customerCode; // 顧客コード
	
	@Pattern(regexp = "^[^\\p{javaWhitespace}]+", message = "空白文字は使用できません。")
	@Size(min = 1, max = 128)
	private String name; // 顧客名
	
	@Pattern(regexp = "^\\d{3}[-]{0,1}\\d{4}$", message = "郵便番号の形式で入力して下さい。(ハイフン無しでも可)")
	@NotBlank(groups = ValidGroup1.class)
	private String postalCode; // 郵便番号
	
	private String address; // 住所
	
	@Size(max = 8)
	@NotBlank(groups = ValidGroup1.class)
	private String firstAddress; // 都道府県
	
	@Size(max = 128)
	@NotBlank(groups = ValidGroup1.class)
	private String secondAddress; // 市区町村
	
	@Size(max = 255)
	@NotBlank(groups = ValidGroup1.class)
	private String thirdAddress; // 町名番地
	
	@Pattern(regexp = "^0\\d{1,4}-\\d{1,4}-\\d{4}$", message = "電話番号の形式で入力して下さい。")
	@NotBlank(groups = ValidGroup1.class)
	private String phoneNumber; // 電話番号
}
