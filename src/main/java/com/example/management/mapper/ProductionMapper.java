package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Production;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ProductionMapper {

	/**
	 * 製作手配テーブルの全てのレコードを取得する。
	 * 
	 * @return List<Production> リスト型の製作手配
	 */
	public List<Production> findAll();
	
	/**
	 * 製作手配テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * 
	 * @return List<Production> リスト型の製作手配クラス
	 */
	public List<Production> findByConditions(String lotNumber, String itemCode, String itemName);
	
	/**
	 * 製作番号を基に製作手配テーブルから合致したレコードを取得する。
	 * 
	 * @param lotNumber 製作番号
	 * 
	 * @return Production 製作手配クラス
	 */
	public Production findByLotNumber(String lotNumber);
	
	/**
	 * IDを基に製作手配テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Production 製作手配クラス
	 */
	public Production findById(Long id);
	
	/**
	 * 製作番号の最新データを取得する。
	 * 
	 * @return 最新の製作番号
	 */
	public String getLotNumberByLatest();
	
	/**
	 * 製作手配テーブルに1件新規登録する。
	 * 
	 * @param production 製作手配クラス
	 */
	public void create(Production production);
	
	/**
	 * 製作手配テーブルを1件更新する。
	 * 
	 * @param production 製作手配クラス
	 */
	public void update(Production production);
	
	/**
	 * 製作手配テーブルから1件論理削除する。
	 * 
	 * @param production　製作手配クラス
	 */
	public void delete(Production production);
}
