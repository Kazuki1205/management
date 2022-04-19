package com.example.management.mapper;

import java.util.List;
import java.util.Optional;

import com.example.management.model.Storing;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface StoringMapper {

	/**
	 * 入庫テーブルから、IDを基にレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Storing 入庫クラス
	 */
	public Storing findById(Long id);
	
	/**
	 * 入庫テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * 
	 * @return List<Storing> リスト型の入庫クラス
	 */
	public List<Storing> findByConditions(String lotNumber, String itemCode, String itemName);
	
	/**
	 * 入庫テーブルを、製作IDでグループ化し、
	 * 入庫数を集計する。
	 * 引数の製作IDと合致したレコードの合計値を取得する。
	 * 
	 * @param productionId 製作ID
	 * 
	 * @return Optional<Integer> 製作IDで集計をかけた入庫数
	 */
	public Optional<Integer> sumOfStoringQuantity(Long productionId);
	
	/**
	 * 入庫テーブルに1件データを挿入する。
	 * 
	 * @param storing 入庫クラス
	 */
	public void create(Storing storing);
	
	/**
	 * 入庫テーブルの1件データを更新する。
	 * 
	 * @param storing 入庫クラス
	 */
	public void update(Storing storing);
}
