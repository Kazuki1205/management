package com.example.management.mapper;

import com.example.management.model.CustomerHistory;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface CustomerHistoryMapper {

	/**
	 * IDを基に、顧客履歴テーブルの最新レコードを取得する。
	 * 
	 * @param customerId 顧客ID
	 * 
	 * @return 顧客履歴クラス
	 */
	public CustomerHistory findByCustomerIdByLatest(Long customerId);
}
