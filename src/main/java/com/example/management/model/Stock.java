package com.example.management.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * 在庫クラス
 */
@Data
public class Stock {

	private Long id; // ID
	
	private Item item; // 商品クラス
	
	private List<OrderDetail> orderDetails; // 受注明細クラスのリスト
	
	private Integer actualQuantity; // 実在庫
	
	private Integer validOrderQuantityTotal; // 受注状況が「削除済・出荷済」でない受注明細テーブルの受注数合計
	
	private Integer validShippingQuantityTotal; // 受注状況が「削除済・出荷済」でない出荷テーブルの出荷数合計
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
}
