package com.example.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * 受注クラス
 */
@Data
public class Order {

	private Long id; // ID
	
	private String orderNumber; // 受注番号
	
	private Integer orderAmountTotal; // 受注金額合計
	
	private List<OrderDetail> orderDetails; // 受注明細クラスのリスト
	
	private CustomerHistory customerHistory; // 顧客履歴クラス
	
	private EmployeeHistory employeeHistory; // 社員履歴クラス
	
	private LocalDate completionDate; // 完了日
	
	private LocalDateTime createdAt; // 登録日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ
}
