package com.example.management.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;

/**
 * 日報入力画面の入力フォーム用クラス
 */
@Data
public class ReportForm {

	private Long id; // ID
	
	private String itemCode; // 品コード
	
	private Long departmentId; // 部署名
	
	private String employeeName; // 社員名
	
	private String lotNumber; // 製作番号
	
	private Integer lotQuantity; // 製作数
	
	@NotNull(message = "製作番号を選択して下さい。")
	private Long productionId; // 製作手配ID
	
	@NotBlank(message = "品名が入力されていません。")
	private String itemName; // 商品名
	
	@NotNull(message = "値を入力して下さい。")
	@PositiveOrZero(message = "「0」以上の値を入力して下さい。")
	private Integer completionQuantity; // 完了数
	
	@NotNull(message = "値を入力して下さい。")
	@PositiveOrZero(message = "「0」以上の値を入力して下さい。")
	private Integer failureQuantity; // 不良数
}
