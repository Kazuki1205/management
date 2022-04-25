package com.example.management.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

/**
 * 受注画面の明細入力フォーム用クラス
 */
@Data
public class OrderDetailForm {
	
	private Long detailId; // 受注明細ID
	
	private String itemName; // 商品名

	@NotNull(message = "商品を選択して下さい。")
	private Long itemId; // 商品ID
	
	private Long unitPrice; // 商品単価
	
	@NotNull(message = "数量を入力して下さい。")
	@Positive(message = "「1」以上の数値を設定して下さい。")
	private Integer orderQuantity; // 受注数量
	
	private String orderAmount; // 受注小計
	
	private Integer shippingQuantityTotal = 0; // 出荷済数
	
	private Short invalid; // 削除フラグ
}
