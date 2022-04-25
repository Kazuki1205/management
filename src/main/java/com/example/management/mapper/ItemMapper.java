package com.example.management.mapper;

import java.util.List;
import java.util.Optional;

import com.example.management.model.Item;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ItemMapper {
	
	/**
	 * 商品テーブルの全てのレコードを取得する。
	 * 削除済みは除く
	 * 
	 * @return List<Item> リスト型の商品クラス
	 */
	public List<Item> findAllExcludeInvalid();
	
	/**
	 * 商品テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param code 商品コード
	 * @param name 商品名
	 * 
	 * @return List<Item> リスト型の商品クラス
	 */
	public List<Item> findByConditions(String code, String name);
	
	/**
	 * IDを基に商品テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Item 商品クラス
	 */
	public Item findById(Long id);

	/**
	 * IDを基に商品テーブルから合致したレコードを取得する。
	 * 削除済みは除く
	 * 
	 * @param id ID
	 * 
	 * @return Item 商品クラス
	 */
	public Item findByIdExcludeInvalid(Long id);
	
	/**
	 * 商品コードを基に商品テーブルから合致したレコードを取得する。
	 * 
	 * @param code 商品コード
	 * 
	 * @return Item 商品クラス
	 */
	public Item findByCode(String code);
	
	/**
	 * 商品テーブルの全レコード数を取得する。
	 * 
	 * @return Optional<Integer> レコード数
	 */
	public Optional<Integer> countAll();
	
	/**
	 * 商品テーブルに1件新規登録する。
	 * 
	 * @param item 商品クラス
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
