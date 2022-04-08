package com.example.management.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.management.model.Employee;
import com.example.management.form.EmployeeForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.EmployeeMapper;

/**
 * 社員マスタのロジッククラス
 */
@Service
public class EmployeeService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * 社員情報の新規登録メソッド
	 * 社員フォームの情報をセットする。
	 * パスワードはエンコードを掛ける。
	 * DBへ登録
	 * 
	 * @param employeeForm 社員フォーム
	 */
	public void createEmployee(EmployeeForm employeeForm) {
		
		// 社員フォームから社員クラスに情報の詰め替え。(パスワードはエンコードする)
		Employee employee = employeeModelMapping(employeeForm, 1);
		
		employeeMapper.create(employee);
	}
	
	/**
	 * 社員情報の更新メソッド
	 * newした社員クラスに、社員フォームの情報をセットする。
	 * DBの更新
	 * 
	 * @param employeeForm　社員フォーム	
	 */
	public void updateEmployee(EmployeeForm employeeForm) {
		
		// 社員フォームから社員クラスに情報の詰め替え。
		Employee employee = employeeModelMapping(employeeForm, 0);
		
		employeeMapper.update(employee);
	}
	
	/**
	 * 社員情報の更新メソッド
	 * 社員フォームの情報をセットする。(パスワードのみ)
	 * DBの更新
	 * 
	 * @param employeeForm　社員フォーム	
	 */
	public void updateEmployeePassword(EmployeeForm employeeForm) {
		
		// 社員フォームから社員クラスに情報の詰め替え。(パスワードはエンコードする)
		Employee employee = employeeModelMapping(employeeForm, 1);
		
		employeeMapper.updatePassword(employee);
	}
	
	/**
	 * 社員情報の削除メソッド
	 * 社員クラスに、社員フォームの情報をセットする。
	 * DBから削除(論理削除)
	 * 
	 * @param employeeForm 社員フォーム
	 */
	public void deleteEmployee(EmployeeForm employeeForm) {
		
		Employee employee = new Employee();
		
		// 社員フォームから社員クラスにIDをセットする。
		employee.setId(employeeForm.getId());
		
		employeeMapper.delete(employee);
	}
	
	/**
	 * 社員テーブルの全レコード数をInteger型で取得し、
	 * その値に+1をした上で[0000」の形に整形する。
	 * 
	 * @return username 社員ID
	 */
	public String getEmployeeUsername() {
		
		// 社員テーブルの全レコード数 + 1
		Integer tempUsername = employeeMapper.countAll() + 1;
		
		//　「0000」の形の文字列に整形。
		String username = String.format("%04d", tempUsername);

		return username;
	}

	/**
	 * 受け取った社員フォームから社員クラスに情報の詰め替えを行うメソッド
	 * 
	 * @param employeeForm　社員フォーム
	 * @param encodeFlag 社員フォームがパスワードの情報を持つ場合は「1」、無ければ「0」
	 * 
	 * @return 社員クラス
	 */
	private Employee employeeModelMapping(EmployeeForm employeeForm, Integer encodeFlag) {
		
		Employee employee = new Employee();
		
		// 社員フォームから社員クラスに情報を詰め替える。
		employee = modelMapper.map(employeeForm, Employee.class);
		
		// フラグが立っていればパスワードをハッシュ化する。
		if (encodeFlag == 1) {
			employee.setPassword(passwordEncoder.encode(employeeForm.getPassword()));
		}
		
		// 部署IDを基に部署情報をセットする。
		employee.setDepartment(departmentMapper.findById(employeeForm.getDepartmentId()));
		
		return employee;
	}
}	
