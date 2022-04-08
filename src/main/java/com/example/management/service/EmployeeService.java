package com.example.management.service;

import java.util.Date;

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
	 * newした社員クラスに、社員フォームの情報をセットする。
	 * パスワードはエンコードを掛ける。
	 * DBへ登録
	 * 
	 * @param employeeForm 社員フォーム
	 */
	public void createEmployee(EmployeeForm employeeForm) {
		
//		employee.setPassword(passwordEncoder.encode(employeeForm.getPassword()));
//		employee.setDepartment(departmentMapper.findById(employeeForm.getDepartmentId()));
//		employee.setUsername(employeeForm.getUsername());
//		employee.setName(employeeForm.getName());
		
		Employee employee = new Employee();
		
		employee = modelMapper.map(employeeForm, Employee.class);
		employee.setPassword(passwordEncoder.encode(employeeForm.getPassword()));
		employee.setDepartment(departmentMapper.findById(employeeForm.getDepartmentId()));
		
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
		
		Employee employee = new Employee();
		
		employee = modelMapper.map(employeeForm, Employee.class);
		employee.setDepartment(departmentMapper.findById(employeeForm.getDepartmentId()));
		
		employeeMapper.update(employee);
	}
	
	/**
	 * 社員情報の削除メソッド
	 * newした社員クラスに、社員フォームの情報をセットする。
	 * DBから削除(論理削除)
	 * 
	 * @param employeeForm 社員フォーム
	 */
	public void deleteEmployee(EmployeeForm employeeForm) {
		
		Employee employee = new Employee();
		employee.setId(employeeForm.getId());
		
		employeeMapper.delete(employee);
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
