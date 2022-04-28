package com.example.management.mapper;

import com.example.management.model.Shipping;
import com.example.management.model.Storing;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface StockMapper {
	
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
