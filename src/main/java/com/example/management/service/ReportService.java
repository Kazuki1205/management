package com.example.management.service;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.management.form.ReportForm;
import com.example.management.mapper.EmployeeHistoryMapper;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.mapper.StoringMapper;
import com.example.management.model.Employee;
import com.example.management.model.Production;
import com.example.management.model.Report;

/**
 * 日報のロジッククラス
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
	
	@Autowired
	private StoringMapper storingMapper;
	
	/**
	 * 日報テーブルに1件新規登録するメソッド
	 * 「不良数計 + 入庫数計 >= 製作数」となったら、製作完了とし、
	 * 製作テーブルの製作完了日を更新する。
	 * 
	 * @param reportForm 日報フォーム
	 * @param employeeId 社員ID
	 */
	@Transactional(rollbackForClassName = "{Exception}")
	public void create(ReportForm reportForm, Long employeeId) {
		
		// 日報フォームから日報クラスに値を詰め替え、DBへinsert処理を行う。
		reportMapper.create(modelMapping(reportForm, employeeId));
		
		// 製作テーブルの製作完了を更新するかを判別し、必要であれば更新するメソッドへ渡す。
		updateProduction(reportForm);
	}
	
	/**
	 * 日報テーブルをに1件更新するメソッド
	 * 「不良数計 + 入庫数計 >= 製作数」となったら、製作完了とし、
	 * 製作テーブルの製作完了日を更新する。
	 * 
	 * @param reportForm 日報フォーム
	 * @param employeeId 社員ID
	 */
	@Transactional(rollbackForClassName = "{Exception}")
	public void update(ReportForm reportForm, Long employeeId) {
		
		// 日報フォームから日報クラスに値を詰め替え、DBへupdate処理を行う。
		reportMapper.update(modelMapping(reportForm, employeeId));
		
		// 製作テーブルの製作完了を更新するかを判別し、必要であれば更新するメソッドへ渡す。
		updateProduction(reportForm);
	}
	
	/**
	 * 日報テーブルをに1件削除するメソッド
	 * 
	 * @param reportForm 日報フォーム
	 */
	public void delete(ReportForm reportForm) {
		
		Report report = new Report();
		
		// 日報クラスにIDをセット
		report.setId(reportForm.getId());
		
		reportMapper.delete(report);
	}
	
	/**
	 * 日報フォームから日報クラスへ値の詰め替えを行うメソッド。
	 * 
	 * @param reportForm 日報フォーム
	 * 
	 * @return 日報クラス
	 */
	private Report modelMapping(ReportForm reportForm, Long employeeId) {
		
		// 日報フォームから日報クラスへ値の詰め替え。
		Report report = modelMapper.map(reportForm, Report.class);
		
		// 製作クラスをセットする。
		report.setProduction(productionMapper.findByIdExcludeInvalidAndCompletion(reportForm.getProductionId()));	
		
		// 社員履歴クラスをセットする。
		report.setEmployeeHistory(employeeHistoryMapper.findByEmployeeIdByLatest(employeeId));
		
		return report;
	}
	
	/**
	 * 引数の日報フォームの製作IDに該当する製作テーブルのレコードの
	 * 製作完了日を更新するメソッド。
	 * 「不良数計 + 入庫数計 >= 製作数」の場合のみ、更新を行う。
	 * 
	 * @param reportForm 日報フォーム
	 */
	private void updateProduction(ReportForm reportForm) {
		
		// フォームから製作IDを取得。
		Long productionId = reportForm.getProductionId();
		
		// 現在の製作数を取得する。
		Integer lotQuantity = productionMapper.getLotQuantity(productionId);
		
		// 現在の不良数計を取得する。
		Integer failureQuantityTotal = reportMapper.sumOfFailureQuantity(productionId).orElse(0);
		
		// 現在の入庫数計を取得する。
		Integer storingQuantityTotal = storingMapper.sumOfStoringQuantity(productionId).orElse(0);
		
		// 「不良数計 + 入庫数計 >= 製作数」であれば製作完了とし、製作テーブルの該当レコードの製作完了日を更新する。
		if (failureQuantityTotal + storingQuantityTotal >= lotQuantity) {
			
			// 製作クラスを用意。
			Production production = new Production();
			
			// 製作ID・現在日付をセット。
			production.setId(productionId);
			production.setCompletionDate(LocalDate.now());
			
			// DBへupdate処理
			productionMapper.updateCompletion(production);	
		}		
	}
	
	/**
	 * 引数の日報クラス・社員クラスを、
	 * 日報フォームへ詰め替えるメソッド。
	 * 
	 * @param report 日報クラス
	 * @param employee 社員クラス
	 * 
	 * @return reportForm 日報フォーム
	 */
	public ReportForm formMapping(Report report, Employee employee) {
		
		// モデルマッパーを完全一致のみにする。
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.typeMap(Report.class, ReportForm.class).addMappings(mapper -> mapper.skip(ReportForm::setProductionId));
		
		// 日報クラスから日報フォームへ詰め替える。
		ReportForm reportForm = modelMapper.map(report, ReportForm.class);
		
		// 取得した日報クラスから製作クラスを取り出す。
		Production production = report.getProduction();
		
		// フォームにそれぞれ製作ID・製作番号・・商品コード・商品名・製作数をセットする。
		reportForm.setProductionId(production.getId());
		reportForm.setLotNumber(production.getLotNumber());
		reportForm.setItemCode(production.getItem().getCode());
		reportForm.setItemName(production.getItem().getName());
		reportForm.setLotQuantity(production.getLotQuantity());
		
		// フォームに日報テーブルの完了数計(製作IDと部署IDでグループ化したものの集計)をセットする。nullなら0とする。
		reportForm.setDepartmentCompletionQuantityTotal(reportMapper.sumOfCompletionQuantity(production.getId(), employee.getDepartment().getId()).orElse(0));
		
		// フォームに日報テーブルの不良数計(製作IDでグループ化したものの集計)をセットする。nullなら0とする。
		reportForm.setFailureQuantityTotal(reportMapper.sumOfFailureQuantity(production.getId()).orElse(0));
		
		return reportForm;
	}
}
