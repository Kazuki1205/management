package com.example.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 受注明細クラス
 */
@Data
public class OrderDetail {
	
	private String orderNumber; // 受注番号
	
	private Integer detailId; // 受注明細ID
	
	private Order order; // 受注クラス
	
	private ItemHistory itemHistory; // 商品履歴クラス
	
	private Integer orderQuantity; // 受注数
	
	private Integer shippingQuantityTotal; // 出荷数計
	
	private LocalDate completionDate; // 出荷完了日
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ
}
