package com.example.management.mapper;

import com.example.management.model.ItemHistory;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ItemHistoryMapper {

	/**
	 * 商品履歴テーブルから、引数のIDで絞り込み、
	 * 更新日時が最新のレコードの情報を取得する。
	 * 
	 * @param itemId 商品ID
	 * 
	 * @return ItemHistory 商品履歴クラス
	 */
	public ItemHistory findByItemIdByLatest(Long itemId);
}
