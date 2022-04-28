package com.example.management.form;

import java.util.Optional;

import lombok.Data;

/**
 * 出荷画面の明細入力フォーム用クラス
 */
@Data
public class ShippingDetailForm {
	
	private Integer detailId; // 受注明細ID
	
	private Long itemId; // 商品ID

	private String itemCode; // 商品コード
	
	private String itemName; // 商品名
	
	private Long unitPrice; // 単価
	
	private Integer orderQuantity; // 受注数
	
	private Integer shippingQuantityTotal; // 出荷数計
	
	private Long shippingAmount; // 出荷金額小計
	
	private Integer actualQuantity; // 実在庫
	
	private Integer shippingQuantity; // 出荷数
}
