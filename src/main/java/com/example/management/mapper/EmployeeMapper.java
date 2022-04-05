package com.example.management.mapper;

import java.util.List;

import com.example.management.model.Employee;

public interface EmployeeMapper {

	public List<Employee> findAll();
	
	public Employee findByUsername(String username);
	
	public void insert(Employee employee);
}
