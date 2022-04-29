package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Shipping;
import com.example.management.model.Stock;
import com.example.management.model.Storing;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface StockMapper {
	
	/**
	 * 引数の商品コード・商品名を基に、在庫テーブルをあいまい検索する。
	 * 
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * 
	 * @return List<Stock> 在庫クラスのリスト
	 */
	public List<Stock> findByConditions(String itemCode, String itemName);
	
	/**
	 * 引数の在庫IDを基に、該当レコードを取得する。
	 * 
	 * @param id 在庫ID
	 * 
	 * @return Stock 在庫クラス
	 */
	public Stock findById(Long id);
	
	/**
	 * 引数の商品IDと合致したレコードの実在庫を返す。
	 * 
	 * @param itemId 商品ID
	 * 
	 * @return Integer 実在庫
	 */
	public Integer getActualQuantity(Long itemId);

	/**
	 * 在庫テーブルの実在庫に、引数の入庫数を加算する。
	 * 
	 * @param storing 入庫クラス
	 */
	public void updateStoring(Storing storing);
	
	/**
	 * 在庫テーブルの実在庫から、引数の出荷数で減算する。
	 * 
	 * @param Shipping 出荷クラス
	 */
	public void updateShipping(Shipping Shipping);
}
