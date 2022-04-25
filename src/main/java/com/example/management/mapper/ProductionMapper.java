package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Production;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ProductionMapper {
	
	/**
	 * 製作テーブルの全てのレコードを取得する。
	 * 削除済みを除く
	 * 
	 * @return List<Production> リスト型の製作
	 */
	public List<Production> findAllExcludeInvalid();
	
	/**
	 * 製作テーブルの全てのレコードを取得する。
	 * 削除済み・完了済みを除く
	 * 
	 * @return List<Production> リスト型の製作
	 */
	public List<Production> findAllExcludeInvalidAndCompletion();
	
	/**
	 * 製作テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * 
	 * @return List<Production> リスト型の製作クラス
	 */
	public List<Production> findByConditions(String lotNumber, String itemCode, String itemName);
	
	/**
	 * 製作番号を基に製作テーブルから合致したレコードを取得する。
	 * 
	 * @param lotNumber 製作番号
	 * 
	 * @return Production 製作クラス
	 */
	public Production findByLotNumber(String lotNumber);
	
	/**
	 * IDを基に製作テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Production 製作クラス
	 */
	public Production findById(Long id);
	
	/**
	 * IDを基に製作テーブルから合致したレコードを取得する。
	 * 削除済みを除く
	 * 
	 * @param id ID
	 * 
	 * @return Production 製作クラス
	 */
	public Production findByIdExcludeInvalid(Long id);
	
	/**
	 * IDを基に製作テーブルから合致したレコードを取得する。
	 * 削除済み・完了済みを除く
	 * 
	 * @param id ID
	 * 
	 * @return Production 製作クラス
	 */
	public Production findByIdExcludeInvalidAndCompletion(Long id);
	
	/**
	 * 製作番号の最新データを取得する。
	 * 
	 * @return 最新の製作番号
	 */
	public String getLotNumberByLatest();
	
	/**
	 * IDを基に製作テーブルの製作数を取得する。
	 * 
	 * @param id ID
	 * 
	 * @return 製作数
	 */
	public Integer getLotQuantity(Long id);
	
	/**
	 * 製作テーブルに1件新規登録する。
	 * 
	 * @param production 製作クラス
	 */
	public void create(Production production);
	
	/**
	 * 製作テーブルを1件更新する。
	 * 
	 * @param production 製作クラス
	 */
	public void update(Production production);
	
	/**
	 * 製作テーブルを1件更新する。
	 * 完了日を更新する。
	 * 
	 * @param production 製作クラス
	 */
	public void updateCompletion(Production production);
	
	/**
	 * 製作テーブルから1件論理削除する。
	 * 
	 * @param production　製作クラス
	 */
	public void delete(Production production);
}
