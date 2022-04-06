package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Department;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface DepartmentMapper {

	/**
	 * 部署テーブルのレコードを全取得する。(削除済を除く)
	 * 
	 * @return List<Employee> 部署クラス
	 */
	public List<Department> findAll();
	
	/**
	 * IDを基に部署テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Department 部署クラス
	 */
	public Department findById(Long id);
}
