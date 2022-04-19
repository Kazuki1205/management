package com.example.management.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

/**
 * 入庫画面の入力フォーム用クラス
 */
@Data
public class StoringForm {

	private Long id; // ID
	
	private String lotNumber; // 製作番号
	
	private String itemCode; // 商品コード
	
	private String itemName; // 商品名
	
	private Integer lotQuantity; // 製作数
	
	private Integer storingQuantityTotal; // 入庫テーブルを製作IDでグループ化した際の入庫数集計結果
	
	private Integer failureQuantityTotal; // 日報テーブルを製作IDでグループ化した際の不良数集計結果
	
	private Short completionInput = 0; // 製番完了区分
	
	@NotNull(message = "製作番号を選択して下さい。")
	private Long productionId; // 製作ID
	
	@NotNull(message = "値を入力して下さい。")
	@Positive(message = "「1」以上の値を入力して下さい。")
	private Integer storingQuantity; // 入庫数
	
	
}
