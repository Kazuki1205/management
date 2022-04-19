package com.example.management.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.management.form.StoringForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.StockMapper;
import com.example.management.mapper.StoringMapper;
import com.example.management.model.Production;
import com.example.management.model.Storing;

/**
 * 入庫のロジッククラス
 */
@Service
public class StoringService {

	@Autowired
	private StoringMapper storingMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private StockMapper stockMapper;
	
	/**
	 * 入庫テーブルの新規登録、製作テーブルに製番完了フラグ更新、在庫テーブルの実在庫更新
	 * の3つの処理を行うメソッド。
	 * 
	 * DBへ登録
	 * 
	 * @param storingForm 入庫フォーム
	 * @param production 製作クラス
	 */
	@Transactional(rollbackForClassName={"Exception"})
	public void create(StoringForm storingForm, Production production) {
		
		// 入庫フォームから入庫クラスへ値の詰め替え。
		Storing storing = modelMapper.map(storingForm, Storing.class);
		
		// 製作クラスを入庫クラスにセットする。
		storing.setProduction(production);
		
		// 入庫テーブルへ挿入処理
		storingMapper.create(storing);
		
		// 在庫テーブルの商品IDと合致したレコードの実在庫をマイナスする。
		stockMapper.updateStoring(storing);
		
		// 入庫フォームの完了区分「0」未完了・「1」完了済を、製作クラスにセットする。
		production.setCompletionFlag(storingForm.getCompletionInput());
	
		// 製作テーブルの製作IDと合致したレコードの完了区分を更新する。
		productionMapper.updateCompletion(production);
	}
	
	/**
	 * 製作テーブルに製番完了フラグ更新を行うメソッド。
	 * 
	 * DBへ登録
	 * 
	 * @param storingForm 入庫フォーム
	 * @param production 製作クラス
	 */
	@Transactional(rollbackForClassName={"Exception"})
	public void update(StoringForm storingForm, Production production) {
		
		// 入庫フォームから入庫クラスへ値の詰め替え。
		Storing storing = modelMapper.map(storingForm, Storing.class);
		
		// 製作クラスを入庫クラスにセットする。
		storing.setProduction(production);
		
		// 入庫テーブルの更新処理
		storingMapper.update(storing);
		
		// 入庫フォームの完了区分「0」未完了・「1」完了済を、製作クラスにセットする。
		production.setCompletionFlag(storingForm.getCompletionInput());
	
		// 製作テーブルの製作IDと合致したレコードの完了区分を更新する。
		productionMapper.updateCompletion(production);
	}
}
