package com.candidjava.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candidjava.spring.bean.Employee;

import com.candidjava.spring.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeServiceImp implements EmployeeService {
	@Autowired
	EmployeeRepository employeeRepository;
	Employee employee;

	public List<Employee> getAllEmployees() {
		return (List<Employee>) employeeRepository.findAll();
	}

	public Employee updateEmployeeDetails(Employee employee, long l) {

		return employeeRepository.save(employee);
	}

	public Employee deleteEmployeeById(long id) {

		employeeRepository.delete(id);
		return employee;
	}

	public boolean saveEmployee(Employee employee) {

		employeeRepository.save(employee);
		return true;
	}

	@Override
	public Employee findEmployeeById(long id) {

		return employeeRepository.findOne(id);

	}

}
