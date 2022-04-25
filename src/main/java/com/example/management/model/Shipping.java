package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 出荷クラス
 */
@Data
public class Shipping {
	
	private Long id; // ID
	
	private OrderDetail orderDetail; // 受注明細クラス
	
	private Integer shippingQuantity; // 出荷数
	
	private LocalDateTime createdAt; // 登録日時
}
