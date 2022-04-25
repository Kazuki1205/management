package com.example.management.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 顧客履歴クラス
 */
@Data
public class CustomerHistory {
	
	private Long id; // ID
	
	private Long customerId; // 顧客ID(顧客テーブルの主キー)
	
	private String code; // 顧客コード
	
	private String name; // 顧客名
	
	private String postalCode; // 郵便番号
	
	private String firstAddress; // 都道府県
	
	private String secondAddress; // 市区町村
	
	private String thirdAddress; // 町名番地
	
	private String phoneNumber; // 電話番号
	
	private LocalDateTime updatedAt; // 更新日時

}
