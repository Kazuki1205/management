package com.example.management.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.management.form.ProductionForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.mapper.StoringMapper;
import com.example.management.model.Production;
import com.example.management.model.Report;

/**
 * 製作のロジッククラス
 */
@Service
public class ProductionService {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ReportMapper reportMapper;
	
	@Autowired
	private StoringMapper storingMapper;
	
	/**
	 * 「年4桁-月2桁-通し番号4桁」形式の製作番号で、
	 * 製作テーブルの最新製番 + 1の文字列を取得する。
	 * 月替りでは「年4桁-月2桁-0001」を取得する。
	 * 
	 * @return lotNumber 製作番号
	 */
	public String getLotNumber() {
		
		// 現在の日付を文字列で取得する。
		String nowDate = new String(LocalDate.now().toString());
		
		// 製作テーブルの最新レコードの製作番号を文字列で取得する。
		String lotNumber = productionMapper.getLotNumberByLatest();
		
		// 最新の製作番号の年月と、現在の年月が一致する場合は、下4桁の通し番号 + 1に置き換える。
		// 一致しない場合は、その月の製作番号が初めて取得される物と識別し、下4桁を「0001」に置き換える。
		if (lotNumber != null && lotNumber.substring(0, 8).equals(nowDate.substring(0, 8))) {
			
			Integer serialNumber = Integer.parseInt(lotNumber.substring(8)) + 1;
			
			lotNumber = nowDate.substring(0, 8) + String.format("%04d", serialNumber);
			
		} else {
			
			lotNumber = nowDate.substring(0, 8) + "0001";
		}
		
		return lotNumber;
	}
	
	/**
	 * 製作情報の新規登録メソッド
	 * DBへ登録
	 * 
	 * @param productionForm 製作フォーム
	 */
	public void create(ProductionForm productionForm) {
		
		//　製作フォームから製作クラスに詰め替え、DBを更新する。
		productionMapper.create(modelMapping(productionForm));
	}
	
	/**
	 * 製作情報の更新メソッド
	 * 製作数を変更した際に、「入庫数計 + 不良数計」以下になる場合は、
	 * 製作が完了したとみなし、完了日も併せて更新する。
	 * 
	 * DBの更新
	 * 
	 * @param productionForm　製作フォーム	
	 */
	public void update(ProductionForm productionForm) {
		
		//　製作フォームから製作クラスに詰め替え。
		Production production = modelMapping(productionForm);
		
		//　DBを更新する。
		productionMapper.update(production);
		
		// 製作フォームの製作数を取得する。
		Integer lotQuantity = productionForm.getLotQuantity();
		
		// 日報テーブルの不良数計(製作IDでグループ化したものの集計)を取得する。nullなら0とする。
		Integer failureQuantityTotal = reportMapper.sumOfFailureQuantity(production.getId()).orElse(0);
		
		// 入庫テーブルの入庫数計(製作IDでグループ化したものの集計)を取得する。nullなら0とする。
		Integer storingQuantityTotal = storingMapper.sumOfStoringQuantity(production.getId()).orElse(0);
		
		// 「入庫数計 + 不良数計 >= 変更した製作数」となった場合、製作完了として、該当レコードの完了日を更新する。
		if (storingQuantityTotal + failureQuantityTotal >= lotQuantity) {
			
			// 現在日付をセット。
			production.setCompletionDate(LocalDate.now());
			
			// 製作テーブルの製作IDと合致したレコードの完了日を更新する。
			productionMapper.updateCompletion(production);
		}
	}
	
	/**
	 * 製作情報の削除メソッド
	 * 製作クラスに、製作フォームの情報をセットする。
	 * DBから削除(論理削除)
	 * 
	 * @param productionForm 製作フォーム
	 */
	public void delete(ProductionForm productionForm) {
		
		Production production = new Production();
		
		// 製作フォームから製作クラスにIDをセットする。
		production.setId(productionForm.getId());
		
		productionMapper.delete(production);
	}
	
	/**
	 * 製作フォームで受け取る日付(文字列)を
	 * LocalDate型に変換してセットする。
	 * 商品IDを基に商品テーブルから該当レコードを取得しセットする。
	 * 
	 * @param productionForm 製作フォーム
	 * 
	 * @return production 製作クラス
	 */
	private Production modelMapping(ProductionForm productionForm) {
		
		// フォームから受け取った日付は文字列型の為、モデルマッパーの詰め替えから除外。
		modelMapper.typeMap(ProductionForm.class, Production.class).addMappings(mapper -> mapper.skip(Production::setScheduledCompletionDate));
		
		// 製作フォームから製作クラスに情報の詰め替え。
		Production production = modelMapper.map(productionForm, Production.class);
		
		// 文字列型の日付をLocalDate型に変換してセット。
		production.setScheduledCompletionDate(LocalDate.parse(productionForm.getScheduledCompletionDate()));
		
		//商品IDを基に商品テーブルから、商品情報を取得する。
		production.setItem(itemMapper.findById(productionForm.getItemId()));
			
		return production;
	}
	
	/**
	 * 引数で受け取った製作クラスのリスト内の
	 * 日報クラスのリストを、テンプレートで各現場工程順に表示される様にする。
	 * 
	 * @param productions 製作クラスのリスト
	 * 
	 * @return List<Production> 整形済みの製作クラスのリスト
	 */
	public List<Production> adjustProductions(List<Production> productions) {
		
		// for内で製作クラスリストを回す。
		for (Production production : productions) {
			
			// 整形後の日報クラスのリスト。最終的にこの変数を製作クラスにsetする。
			List<Report> tempReport = new ArrayList<>(7);
			
			// 全てに空の日報クラスを代入する。
			for (int i = 0; i < 7; i++) {
				
				tempReport.add(new Report());
			}
			
			// 製作クラスから日報リストを取得する。
			List<Report> reports = production.getReports();
			
			// 製作番号に対して日報がない場合は、for内の処理を飛ばす。
			if (reports.get(0).getProductionId() != null) {
			
				// for内で製作クラス内の日報クラスを回す。
				for (Report report : reports) {
					
					// 部署IDを取得
					Long departmentId = report.getDepartmentId();
					
					// 部署テーブルの主キーを基に、、「部署ID2～8(切削～梱包)」を、用意したリスト「index0～6」にセット。
					tempReport.set((int)(departmentId - 2), report);
				}
			}
			
			// 整形後の日報リストをセットする。
			production.setReports(tempReport);
		}
		
		return productions;
	}
}
