package com.example.management.mapper;

import java.util.List;
import java.util.Optional;

import com.example.management.model.Order;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface OrderMapper {
	
	/**
	 * IDを基に受注テーブルから合致したレコードを取得する。
	 * 
	 * @param id　受注ID
	 * 
	 * @return Order 受注クラス
	 */
	public Order findById(Long id);
	
	/**
	 * IDを基に受注テーブルから合致したレコードを取得する。
	 * 削除済みを除く
	 * 
	 * @param id 受注ID
	 * 
	 * @return Order 受注クラス
	 */
	public Order findByIdExcludeInvalid(Long id);
	
	/**
	 * 受注番号を基に受注テーブルから合致したレコードを取得する。
	 * 
	 * @param orderNumber 受注番号
	 * 
	 * @return Order 受注クラス
	 */
	public Order findByOrderNumber(String orderNumber);

	/**
	 * 受注テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param customerCode 顧客コード
	 * @param customerName 顧客名
	 * @param departmentId 部署ID
	 * @param employeeName 社員名
	 * 
	 * @return List<Order> リスト型の受注クラス
	 */
	public List<Order> findByConditions(String orderNumber, String customerCode, String customerName, Long departmentId, String employeeName);
	
	/**
	 * 受注テーブルの全レコード数を取得する。
	 * 
	 * @return Optional<Integer>　全レコード数
	 */
	public Optional<Integer> countAll();
	
	/**
	 * 受注テーブルの最新受注IDを取得する。
	 * 
	 * @return　Long 最新の受注ID
	 */
	public Long getOrderIdByLatest();
	
	/**
	 * 受注テーブルに1件レコードを挿入する。
	 * 
	 * @param order 受注クラス
	 */
	public void create(Order order);
	
	/**
	 * 受注テーブルの該当行のテーブルを削除(論理)する。
	 * 
	 * @param order 受注クラス
	 */
	public void updateInvalid(Order order);
}
