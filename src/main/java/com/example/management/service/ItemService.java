package com.example.management.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.management.form.ItemForm;
import com.example.management.mapper.ItemMapper;
import com.example.management.model.Item;

/**
 * 商品マスタのロジッククラス
 */
@Service
public class ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * 商品テーブルの全レコード数をInteger型で取得し、
	 * その値に+1をした上で[00000000」の形に整形する。
	 * 
	 * @return username 社員ID
	 */
	public String getItemCode() {
		
		// 社員テーブルの全レコード数 + 1
		Integer tempItemCode = itemMapper.countAll() + 1;
		
		//　「00000000」の形の文字列に整形。
		String itemCode = String.format("%08d", tempItemCode);
		
		return itemCode;
	}
	
	/**
	 * 商品情報の新規登録メソッド
	 * 商品フォームの情報をセットする。
	 * DBへ登録
	 * 
	 * @param itemForm 商品フォーム
	 */
	public void createItem(ItemForm itemForm) {
		
		// 商品フォームから商品クラスに情報の詰め替え。
		Item item = modelMapper.map(itemForm, Item.class);
		
		itemMapper.create(item);
	}
	
	/**
	 * 商品情報の更新メソッド
	 * 商品クラスに、商品フォームの情報をセットする。
	 * DBの更新
	 * 
	 * @param itemForm　商品フォーム	
	 */
	public void updateItem(ItemForm itemForm) {
		
		// 商品フォームから商品クラスに情報の詰め替え。
		Item item = modelMapper.map(itemForm, Item.class);
		
		itemMapper.update(item);
	}
	
	/**
	 * 商品情報の削除メソッド
	 * 商品クラスに、商品フォームの情報をセットする。
	 * DBから削除(論理削除)
	 * 
	 * @param itemForm しフォーム
	 */
	public void deleteItem(ItemForm itemForm) {
		
		Item item = new Item();
		
		// 商品フォームから商品クラスにIDをセットする。
		item.setId(itemForm.getId());
		
		itemMapper.delete(item);
	}
}
