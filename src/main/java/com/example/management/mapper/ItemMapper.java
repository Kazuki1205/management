package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Item;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ItemMapper {

	/**
	 * 商品テーブルの全てのレコードを取得する。
	 * 
	 * @return List<Item> リスト型の商品クラス
	 */
	public List<Item> findAll();
	
	/**
	 * 商品テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param itemCode 商品コード
	 * @param name 商品名
	 * 
	 * @return List<Item> リスト型の商品クラス
	 */
	public List<Item> findByConditions(String itemCode, String name);
	
	/**
	 * IDを基に商品テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Item 商品クラス
	 */
	public Item findById(Long id);
	
	/**
	 * 商品コードを基に商品テーブルから合致したレコードを取得する。
	 * 
	 * @param itemCode 商品コード
	 * 
	 * @return Item 商品クラス
	 */
	public Item findByItemCode(String itemCode);
	
	/**
	 * 商品テーブルの全レコード数を取得する。
	 * 
	 * @return Integer レコード数
	 */
	public Integer countAll();
	
	/**
	 * 商品テーブルに1件新規登録する。
	 * 
	 * @param item
	 */
	public void create(Item item);
	
	/**
	 * 商品テーブルを1件更新する。
	 * 
	 * @param item 商品クラス
	 */
	public void update(Item item);
	
	/**
	 * 商品テーブルから1件論理削除する。
	 * 
	 * @param item　商品クラス
	 */
	public void delete(Item item);
}