package com.example.management.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 出荷画面の入力フォーム用クラス
 */
@Data
public class ShippingForm {

	private Long id; // ID
	
	private Integer detailId; // 受注明細ID
	
	private String customerCode; // 顧客コード
	
	private String customerName; // 顧客名
	
	private String address; // 住所
	
	private String firstAddress; // 都道府県
	
	private String secondAddress; // 市区町村
	
	private String thirdAddress; // 町名番地
	
	private String itemCode; // 商品コード
	
	private String itemName; // 商品名
	
	private String selectOrderNumber; // 検索項目の受注番号
	
	@NotBlank(message = "受注番号を選択して下さい。")
	private String orderNumber; // 受注番号
	
	@Valid
	private List<ShippingDetailForm> shippingDetailForms; // 出荷明細フォーム
}
