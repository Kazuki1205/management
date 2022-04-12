package com.example.management.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.management.form.CustomerForm;
import com.example.management.mapper.CustomerMapper;
import com.example.management.model.Customer;

/**
 * 顧客マスタのロジッククラス
 */
@Service
public class CustomerService {
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * 顧客テーブルの全レコード数をInteger型で取得し、
	 * その値に+1をした上で[0000」の形に整形する。
	 * 
	 * @return code 顧客コード
	 */
	public String getCode() {
		
		// 顧客テーブルの全レコード数 + 1
		Integer tempCode = customerMapper.countAll() + 1;
		
		//　「0000」の形の文字列に整形。
		String code = String.format("%04d", tempCode);
		
		return code;
	}
	
	/**
	 * 顧客情報の新規登録メソッド
	 * 顧客フォームの情報をセットする。
	 * 郵便番号の形式を「000-0000」に統一する。
	 * DBへ登録
	 * 
	 * @param customerForm 顧客フォーム
	 */
	public void create(CustomerForm customerForm) {
		
		// 顧客フォームから顧客クラスに情報の詰め替え。
		Customer customer = modelMapper.map(customerForm, Customer.class);
		
		// 郵便番号の形式が「0000000」の様にハイフン無しだった場合、「000-0000」の形式に変更する。
		StringBuilder postalCode = new StringBuilder(customer.getPostalCode());
		
		if (postalCode.indexOf("-") == -1) {
			
			postalCode.insert(3, "-");
			
			customer.setPostalCode(postalCode.toString());
		}
		
		customerMapper.create(customer);
	}
	
	/**
	 * 顧客情報の更新メソッド
	 * 顧客クラスに、顧客フォームの情報をセットする。
	 * DBの更新
	 * 
	 * @param customerForm　顧客フォーム	
	 */
	public void update(CustomerForm customerForm) {
		
		// 顧客フォームから顧客クラスに情報の詰め替え。
		Customer customer = modelMapper.map(customerForm, Customer.class);
		
		customerMapper.update(customer);
	}
	
	/**
	 * 顧客情報の削除メソッド
	 * 顧客クラスに、顧客フォームの情報をセットする。
	 * DBから削除(論理削除)
	 * 
	 * @param customerForm 顧客フォーム
	 */
	public void delete(CustomerForm customerForm) {
		
		Customer customer = new Customer();
		
		// 顧客フォームから顧客クラスにIDをセットする。
		customer.setId(customerForm.getId());
		
		customerMapper.delete(customer);
	}
}
