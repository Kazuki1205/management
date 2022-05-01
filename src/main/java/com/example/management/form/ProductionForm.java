package com.example.management.form;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.example.management.validation.ValidGroup1;
import com.example.management.validation.ValidGroup2;

import lombok.Data;

/**
 * 製作画面の入力フォーム用クラス
 */
@Data
public class ProductionForm {
	
	private Long id; // ID
	
	private String lotNumber; // 製作番号
	
	private String itemCode; // 商品コード

	private String itemName; // 商品名
	
	@NotNull(groups = ValidGroup1.class, message = "商品を選択して下さい。")
	private Long itemId; // 商品ID
	
	@NotNull(groups = ValidGroup1.class, message="入力して下さい。")
	@Positive(groups = ValidGroup2.class, message = "「1」以上の数値を設定して下さい。")
	private Integer lotQuantity; // 製作数
	
	@Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "正しい日付を入力して下さい。")
	private String scheduledCompletionDate; // 完成予定日
}
