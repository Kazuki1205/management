package com.example.management.mapper;

import com.example.management.model.Storing;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface StockMapper {

	/**
	 * 在庫テーブルの実在庫に、引数の入庫数を加算する。
	 * 
	 * @param storing 入庫クラス
	 */
	public void updateStoring(Storing storing);
}
