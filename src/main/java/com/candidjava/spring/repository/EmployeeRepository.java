package com.candidjava.spring.repository;

import org.springframework.data.repository.CrudRepository;

import com.candidjava.spring.bean.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
