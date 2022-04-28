package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 出荷クラス
 */
@Data
public class Shipping {
	
	private Long id; // ID
	
	private Order order; // 受注クラス
	
	private OrderDetail orderDetail; // 受注明細クラス
	
	private Integer shippingQuantity; // 出荷数
	
	private Long shippingAmount; // 出荷金額
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
}
