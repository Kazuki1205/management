package com.example.management.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.management.form.ReportForm;
import com.example.management.mapper.EmployeeHistoryMapper;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.model.Report;

/**
 * 日報入力のロジッククラス
 */
@Service
public class ReportService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmployeeHistoryMapper employeeHistoryMapper;
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ReportMapper reportMapper;
	
	/**
	 * 日報入力テーブルに1件新規登録するメソッド
	 * 
	 * @param reportForm 日報入力フォーム
	 * @param employeeId 社員ID
	 */
	public void create(ReportForm reportForm, Long employeeId) {
		
		// 日報入力フォームから日報入力クラスに値を詰め替え、DBへinsert処理を行う。
		reportMapper.create(modelMapping(reportForm, employeeId));
	}
	
	/**
	 * 日報入力テーブルをに1件更新するメソッド
	 * 
	 * @param reportForm 日報入力フォーム
	 * @param employeeId 社員ID
	 */
	public void update(ReportForm reportForm, Long employeeId) {
		
		// 日報入力フォームから日報入力クラスに値を詰め替え、DBへupdate処理を行う。
		reportMapper.update(modelMapping(reportForm, employeeId));
	}
	
	/**
	 * 日報入力テーブルをに1件削除するメソッド
	 * 
	 * @param reportForm 日報入力フォーム
	 */
	public void delete(ReportForm reportForm) {
		
		Report report = new Report();
		
		// 日報入力クラスにIDをセット
		report.setId(reportForm.getId());
		
		reportMapper.delete(report);
	}
	
	/**
	 * 日報入力フォームから日報入力クラスへ値の詰め替えを行うメソッド
	 * 
	 * @param reportForm 日報入力フォーム
	 * 
	 * @return 日報入力クラス
	 */
	private Report modelMapping(ReportForm reportForm, Long employeeId) {
		
		// 日報入力フォームから日報クラスへ値の詰め替え。
		Report report = modelMapper.map(reportForm, Report.class);
		
		// 製作手配クラスをセットする。
		report.setProduction(productionMapper.findByIdExcludeInvalid(reportForm.getProductionId()));	
		
		// 社員履歴クラスをセットする。
		report.setEmployeeHistory(employeeHistoryMapper.findByUserIdByLatest(employeeId));
		
		return report;
	}
}
