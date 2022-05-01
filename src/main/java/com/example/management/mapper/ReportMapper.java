package com.example.management.mapper;

import java.util.List;
import java.util.Optional;

import com.example.management.model.Report;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ReportMapper {
	
	/**
	 * 日報テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param lotNumber 製作番号
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param departmentId 部署ID
	 * @param employeeName 社員名
	 * 
	 * @return List<Report> リスト型の日報クラス
	 */
	public List<Report> findByConditions(String lotNumber, String itemCode, String itemName, Long departmentId, String employeeName);
	
	/**
	 * IDを基に日報テーブルから合致したレコードを取得する。
	 * 
	 * @param id ID
	 * 
	 * @return Report 日報クラス
	 */
	public Report findById(Long id);
	
	/**
	 * IDを基に日報テーブルから合致したレコードを取得する。
	 * 削除済みを除く、製作テーブルの製作完了済を除く。
	 * 
	 * @param id ID
	 * 
	 * @return Report 日報クラス
	 */
	public Report findByIdExcludeInvalidAndCompletion(Long id);
	
	
	/**
	 * 日報テーブルを、製作IDと部署IDでグループ化し、
	 * 完了数を集計する。
	 * 引数の製作IDと部署IDと合致したレコードの合計値を取得する。
	 * 
	 * @param productionId 製作ID
	 * @param departmentId 部署ID
	 * 
	 * @return Optional<Integer> 製作ID、部署IDで集計をかけた完了数
	 */
	public Optional<Integer> sumOfCompletionQuantity(Long productionId, Long departmentId);
	
	/**
	 * 日報テーブルを、製作IDでグループ化し、
	 * 不良数を集計する。
	 * 引数の製作IDと合致したレコードの合計値を取得する。
	 * 
	 * @param productionId 製作ID
	 * 
	 * @return Optional<Integer> 製作IDで集計をかけた不良数
	 */
	public Optional<Integer> sumOfFailureQuantity(Long productionId);
	
	/**
	 * 日報テーブルに1件新規登録する。 
	 * 
	 * @param report 日報クラス
	 */
	public void create(Report report);
	
	/**
	 * 日報テーブルの1件のデータを更新する。 
	 * 
	 * @param report 日報クラス
	 */
	public void update(Report report);
	
	/**
	 * 日報テーブルの1件のデータを削除(論理)する。 
	 * 
	 * @param report 日報クラス
	 */
	public void delete(Report report);
}
