package com.example.management.service;

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
public class registerEmployeeService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;

	/**
	 * 社員の新規登録メソッド
	 * newした社員クラスに、社員フォームの情報をセットする。
	 * パスワードはエンコードを掛ける。
	 * 
	 * @param employeeForm 社員フォーム
	 */
	public void createEmployee(EmployeeForm employeeForm) {
		
		Employee employee = new Employee();
		employee.setPassword(passwordEncoder.encode(employeeForm.getPassword()));
		employee.setDepartment(departmentMapper.findById(employeeForm.getDepartmentId()));
		employee.setUsername(employeeForm.getUsername());
		employee.setName(employeeForm.getName());
		
		employeeMapper.insert(employee);
	}
	
	/**
	 * 社員テーブルの全レコード数をInteger型で取得し、
	 * その値に+1をした上で[0001」の形に整形する。
	 * 
	 * @return username 社員ID
	 */
	public String getEmployeeUsername() {
		Integer tempUsername = employeeMapper.countAll() + 1;
		
		String username = String.format("%04d", tempUsername);

		return username;
	}
}	
