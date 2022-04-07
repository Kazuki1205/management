package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Employee;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface EmployeeMapper {
	
	/**
	 * 社員IDを基に社員テーブルから合致したレコードを取得する。(削除済を除く)
	 * 
	 * @param username 社員ID
	 * @return Employee 社員クラス
	 */
	public Employee findByUsername(String username);
	
	/**
	 * IDを基に社員テーブルから合致したレコードを取得する。(削除済を除く)
	 * 
	 * @param username ID
	 * @return Employee 社員クラス
	 */
	public Employee findById(Long id);

	/**
	 * @return List<Employee> リスト型の社員クラス
	 */
	public List<Employee> findAll();
	
	/**
	 * 社員テーブルのレコードを、引数のあいまい検索で取得する。
	 * 引数で空のデータを受け取った場合は検索条件から外す。
	 * 
	 * @param username 社員ID
	 * @param name　社員名
	 * @param departmentId　部署ID
	 * 
	 * @return List<Employee> リスト型の社員クラス
	 */
	public List<Employee> findByConditions(String username, String name, Long departmentId);
	
	/**
	 * 社員テーブルの全レコード数を取得する。(削除済を含む)
	 * 
	 * @return Integer レコード数
	 */
	public Integer countAll();
	
	/**
	 * 社員テーブルに1件新規登録する。
	 * 
	 * @param employee 社員クラス
	 */
	public void insert(Employee employee);
	
	/**
	 * 社員テーブルを1件更新する。
	 * 
	 * @param employee 社員クラス
	 */
	public void update(Employee employee);
	
	/**
	 * 社員テーブルから1件削除する。
	 * 
	 * @param employee　社員クラス
	 */
	public void delete(Employee employee);
	

}
