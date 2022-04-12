package com.example.management.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.management.form.DepartmentForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.model.Department;

/**
 * 部署マスタのロジッククラス
 */
@Service
public class DepartmentService {
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * 部署テーブルの全レコード数をInteger型で取得し、
	 * その値に+1をした上で[0000」の形に整形する。
	 * 
	 * @return code 部署コード
	 */
	public String getCode() {
		
		// 部署テーブルの全レコード数 + 1
		Integer tempCode = departmentMapper.countAll() + 1;
		
		//　「0000」の形の文字列に整形。
		String code = String.format("%04d", tempCode);
		
		return code;
	}
	
	/**
	 * 部署情報の新規登録メソッド
	 * 部署フォームの情報をセットする。
	 * DBへ登録
	 * 
	 * @param departmentForm 部署フォーム
	 */
	public void create(DepartmentForm departmentForm) {
		
		// 部署フォームから部署クラスに情報の詰め替え。
		Department department = modelMapper.map(departmentForm, Department.class);
		
		departmentMapper.create(department);
	}
	
	/**
	 * 部署情報の更新メソッド
	 * 部署クラスに、部署フォームの情報をセットする。
	 * DBの更新
	 * 
	 * @param departmentForm　部署フォーム	
	 */
	public void update(DepartmentForm departmentForm) {
		
		// 部署フォームから部署クラスに情報の詰め替え。
		Department department = modelMapper.map(departmentForm, Department.class);
		
		departmentMapper.update(department);
	}
	
	/**
	 * 部署情報の削除メソッド
	 * 部署クラスに、部署フォームの情報をセットする。
	 * DBから削除(論理削除)
	 * 
	 * @param departmentForm 部署フォーム
	 */
	public void delete(DepartmentForm departmentForm) {
		
		Department department = new Department();
		
		// 部署フォームから部署クラスにIDをセットする。
		department.setId(departmentForm.getId());
		
		departmentMapper.delete(department);
	}
}
