package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Report;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ReportMapper {
	
	/**
	 * 日報入力テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param departmentId 部署ID
	 * @param employeeName 社員名
	 * 
	 * @return List<Report> リスト型の日報入力クラス
	 */
	public List<Report> findByConditions(String lotNumber, String itemCode, String itemName, Long departmentId, String employeeName);
	
	/**
	 * IDを基に日報入力テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Report 日報入力クラス
	 */
	public Report findById(Long id);
	
	/**
	 * 日報入力テーブルに1件新規登録する。 
	 * 
	 * @param report 日報入力クラス
	 */
	public void create(Report report);
	
	/**
	 * 日報入力テーブルの1件のデータを更新する。 
	 * 
	 * @param report 日報入力クラス
	 */
	public void update(Report report);
	
	/**
	 * 日報入力テーブルの1件のデータを削除(論理)する。 
	 * 
	 * @param report 日報入力クラス
	 */
	public void delete(Report report);
}
