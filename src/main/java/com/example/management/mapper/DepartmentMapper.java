package com.example.management.mapper;

import java.util.List;
import java.util.Optional;

import com.example.management.model.Department;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface DepartmentMapper {
	
	/**
	 * 部署テーブルのレコードを全取得する。
	 * 削除済みは除く
	 * 
	 * @return List<Department> 部署クラス
	 */
	public List<Department> findAllExcludeInvalid();
	
	/**
	 * IDを基に部署テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Department 部署クラス
	 */
	public Department findById(Long id);
	
	/**
	 * IDを基に部署テーブルから合致したレコードを取得する。
	 * 削除済みは除く
	 * @param id ID
	 * 
	 * @return Department 部署クラス
	 */
	public Department findByIdExcludeInvalid(Long id);
	
	/**
	 * 部署テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param code 部署コード
	 * @param name 部署名
	 * 
	 * @return List<Department> リスト型の部署クラス
	 */
	public List<Department> findByConditions(String code, String name);
	
	/**
	 * 部署コードを基に部署テーブルから合致したレコードを取得する。
	 * 
	 * @param code 部署コード
	 * 
	 * @return Department 部署クラス
	 */
	public Department findByCode(String code);
	
	/**
	 * 部署テーブルの全レコード数を取得する。
	 * 
	 * @return Optional<Integer> レコード数
	 */
	public Optional<Integer> countAll();
	
	/**
	 * 部署テーブルに1件新規登録する。
	 * 
	 * @param department
	 */
	public void create(Department department);
	
	/**
	 * 部署テーブルを1件更新する。
	 * 
	 * @param department 部署クラス
	 */
	public void update(Department department);
	
	/**
	 * 部署テーブルから1件論理削除する。
	 * 
	 * @param department　部署クラス
	 */
	public void delete(Department department);
}
