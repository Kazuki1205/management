package com.example.management.mapper;

import com.example.management.model.EmployeeHistory;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface EmployeeHistoryMapper {
	
	/**
	 * IDを基に、社員履歴テーブルの最新レコードを取得する。
	 * 
	 * @param userId 社員ID
	 * 
	 * @return 社員履歴クラス
	 */
	public EmployeeHistory findByUserIdByLatest(Long employeeId);

}
