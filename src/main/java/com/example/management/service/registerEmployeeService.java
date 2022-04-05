package com.example.management.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.management.entity.Department;
import com.example.management.entity.Employee;
import com.example.management.form.DepartmentForm;
import com.example.management.form.EmployeeForm;
import com.example.management.repository.DepartmentRepository;
import com.example.management.repository.EmployeeRepository;

@Service
public class registerEmployeeService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * 社員フォームオブジェクトを返すメソッド
	 * 
	 * @return 社員フォームオブジェクト
	 */
	public EmployeeForm getEmployeeForm() {
		EmployeeForm employeeForm = new EmployeeForm();
		List<Department> departments = departmentRepository.findAll();
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

		String username = employeeForm.getUsername();
		String password = employeeForm.getPassword();
		String name = employeeForm.getName();
		Long departmentId = Long.parseLong(employeeForm.getDepartmentId());

		Employee employee = new Employee(username, passwordEncoder.encode(password), name, departmentId);

		employeeRepository.saveAndFlush(employee);

	}
}
