package com.example.management.service;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.management.form.ProductionForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.mapper.ProductionMapper;
import com.example.management.model.Production;

/**
 * 製作手配のロジッククラス
 */
@Service
public class ProductionService {
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ItemMapper itemMapper;
	
	/**
	 * 「年4桁-月2桁-通し番号4桁」形式の製作番号で、
	 * 製作手配テーブルの最新製番 + 1の文字列を取得する。
	 * 月替りでは「年4桁-月2桁-0001」を取得する。
	 * 
	 * @return lotNumber 製作番号
	 */
	public String getLotNumber() {
		
		// 現在の日付を文字列で取得する。
		String nowDate = new String(LocalDate.now().toString());
		
		// 製作手配テーブルの最新レコードの製作番号を文字列で取得する。
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
	 * 製作手配情報の新規登録メソッド
	 * DBへ登録
	 * 
	 * @param productionForm 製作手配フォーム
	 */
	public void create(ProductionForm productionForm) {
		
		//　製作手配フォームから製作手配クラスに詰め替え、DBを更新する。
		productionMapper.create(modelMapping(productionForm));
	}
	
	/**
	 * 製作手配情報の更新メソッド
	 * DBの更新
	 * 
	 * @param productionForm　製作手配フォーム	
	 */
	public void update(ProductionForm productionForm) {
		
		//　製作手配フォームから製作手配クラスに詰め替え、DBを更新する。
		productionMapper.update(modelMapping(productionForm));
	}
	
	/**
	 * 製作手配情報の削除メソッド
	 * 製作手配クラスに、製作手配フォームの情報をセットする。
	 * DBから削除(論理削除)
	 * 
	 * @param productionForm 製作手配フォーム
	 */
	public void delete(ProductionForm productionForm) {
		
		Production production = new Production();
		
		// 製作手配フォームから製作手配クラスにIDをセットする。
		production.setId(productionForm.getId());
		
		productionMapper.delete(production);
	}
	
	/**
	 * 製作手配フォームで受け取る日付(文字列)を
	 * LocalDate型に変換してセットする。
	 * 商品IDを基に商品テーブルから該当レコードを取得しセットする。
	 * 
	 * @param productionForm 製作手配フォーム
	 * 
	 * @return production 製作手配クラス
	 */
	private Production modelMapping(ProductionForm productionForm) {
		
		// フォームから受け取った日付は文字列型の為、モデルマッパーの詰め替えから除外。
		modelMapper.typeMap(ProductionForm.class, Production.class).addMappings(mapper -> mapper.skip(Production::setScheduledCompletionDate));
		
		// 製作手配フォームから製作手配クラスに情報の詰め替え。
		Production production = modelMapper.map(productionForm, Production.class);
		
		// 文字列型の日付をLocalDate型に変換してセット。
		production.setScheduledCompletionDate(LocalDate.parse(productionForm.getScheduledCompletionDate()));
		
		//商品IDを基に商品情報をセットする。
		production.setItem(itemMapper.findById(productionForm.getItemId()));
			
		return production;
	}
}
