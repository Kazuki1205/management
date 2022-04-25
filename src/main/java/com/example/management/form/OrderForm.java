package com.example.management.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 受注画面の入力フォーム用クラス
 */
@Data
public class OrderForm {
	
	private Long id; // 受注ID
	
	private String orderNumber; // 受注番号
	
	private String customerCode; // 顧客コード
	
	private String customerName; // 顧客名
	
	private Long departmentId; // 部署ID
	
	private String employeeName; // 社員名
	
	@NotNull(message = "顧客を選択して下さい。")
	private Long customerId; // 顧客ID
	
	@Valid
	private List<OrderDetailForm> orderDetailForms; // 受注明細フォーム

}
