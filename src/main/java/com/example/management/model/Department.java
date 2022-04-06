package com.example.management.model;

import lombok.Data;

/**
 * 部署クラス
 */
@Data
public class Department {
	
	public Department () {} // コンストラクタ(MyBatisで使用)
	
	private Long id; // ID
	
	private String name; // 部署名
}
