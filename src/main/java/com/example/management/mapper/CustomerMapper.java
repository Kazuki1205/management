package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Customer;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface CustomerMapper {

	/**
	 * 顧客テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param code 顧客コード
	 * @param name　顧客名
	 * @param address　住所
	 * 
	 * @return List<Customer> リスト型の顧客クラス
	 */
	public List<Customer> findByConditions(String code, String name, String address);
	
	/**
	 * IDを基に顧客テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Customer 顧客クラス
	 */
	public Customer findById(Long id);
	
	/**
	 * IDを基に顧客テーブルから合致したレコードを取得する。
	 * 削除済みは除く
	 * 
	 * @param id ID
	 * 
	 * @return Customer 顧客クラス
	 */
	public Customer findByIdExcludeInvalid(Long id);
	
	/**
	 * 顧客コードを基に顧客テーブルから合致したレコードを取得する。
	 * 
	 * @param code 顧客コード
	 * 
	 * @return Customer 顧客クラス
	 */
	public Customer findByCode(String code);
	
	/**
	 * テーブルの全レコード数を取得する。
	 * 
	 * @return Integer レコード数
	 */
	public Integer countAll();
	
	/**
	 * 顧客テーブルに1件新規登録する。
	 * 
	 * @param customer 顧客クラス
	 */
	public void create(Customer customer);
	
	/**
	 * 顧客テーブルを1件更新する。
	 * 
	 * @param customer 顧客クラス
	 */
	public void update(Customer customer);
	
	/**
	 * 顧客テーブルから1件論理削除する。
	 * 
	 * @param customer　顧客クラス
	 */
	public void delete(Customer customer);
}
