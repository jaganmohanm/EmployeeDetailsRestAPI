package com.candidjava.spring.service;

import java.util.List;

import com.candidjava.spring.bean.Employee;

public interface EmployeeService {
	public boolean saveEmployee(Employee employee);
	
	public List<Employee> getAllEmployees();

	public Employee updateEmployeeDetails(Employee employee, long l);

	public Employee deleteEmployeeById(long id);

	public Employee findEmployeeById(long id);

}
