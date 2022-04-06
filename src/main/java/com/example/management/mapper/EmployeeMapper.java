package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Employee;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface EmployeeMapper {

	/**
	 * 社員テーブルのレコードを全取得する。(削除済を除く)
	 * 
	 * @return List<Employee> リスト型の社員クラス
	 */
	public List<Employee> findAll();
	
	/**
	 * 社員IDを基に社員テーブルから合致したレコードを取得する。
	 * 
	 * @param username 社員ID
	 * @return Employee 社員クラス
	 */
	public Employee findByUsername(String username);
	
	/**
	 * 社員テーブルに新規登録する。
	 * 
	 * @param employee 社員クラス
	 */
	public void insert(Employee employee);
	
	/**
	 * 社員テーブルの全レコード数を取得する。(削除済を含む)
	 * 
	 * @return Integer レコード数
	 */
	public Integer countAll();
}
