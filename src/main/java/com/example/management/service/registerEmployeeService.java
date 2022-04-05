package com.example.management.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.management.model.Department;
import com.example.management.model.Employee;
import com.example.management.form.DepartmentForm;
import com.example.management.form.EmployeeForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.mapper.EmployeeMapper;

@Service
public class registerEmployeeService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private EmployeeMapper employeeMapper;

	/**
	 * 社員フォームオブジェクトを返すメソッド
	 * 
	 * @return 社員フォームオブジェクト
	 */
	public EmployeeForm getEmployeeForm() {
		
		EmployeeForm employeeForm = new EmployeeForm();
		List<Department> departments = departmentMapper.findAll();
		List<DepartmentForm> departmentForms = new ArrayList<DepartmentForm>();
		
		for (Department department : departments) {
			departmentForms.add(modelMapper.map(department, DepartmentForm.class));
		}
		
		employeeForm.setDepartments(departmentForms);

		return employeeForm;
	}

	/**
	 * 社員の新規登録メソッド
	 * 
	 * @param employeeForm 社員フォーム
	 */
	public void createEmployee(EmployeeForm employeeForm) {
		
		Employee employee = new Employee();
		employee.setPassword(passwordEncoder.encode(employeeForm.getPassword()));
		employee.setDepartmentId(Long.parseLong(employeeForm.getDepartmentId()));
		employee.setUsername(employeeForm.getUsername());
		employee.setName(employeeForm.getName());
		
		employeeMapper.insert(employee);
	}
}
