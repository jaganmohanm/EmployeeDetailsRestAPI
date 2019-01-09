package com.candidjava.spring.controller;

import java.util.List;

import javax.print.attribute.standard.PrinterLocation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.candidjava.spring.bean.Employee;
import com.candidjava.spring.exception.UserException;
import com.candidjava.spring.repository.EmployeeRepository;
import com.candidjava.spring.response.Response;
import com.candidjava.spring.service.EmployeeService;

@RestController

@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	EmployeeService empService;
	Employee emp;
	Response resp;
	private long id;

	@GetMapping(value = "/getAllEmployees", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity getEmployees() {
		List<Employee> employees = empService.getAllEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping(value = "/getEmployeeById/{id}", headers = "Accept=application/json", produces = "application/json")
	public Employee getEmployeeById(@PathVariable("id") long id) {
		Employee emp = empService.findEmployeeById(id);
		return emp;
	}

	@PostMapping(value = "/insertEmployee", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity createUser(@RequestBody Employee emp, UriComponentsBuilder ucBuilder) {
		Response resp = new Response();
		try {
			if (StringUtils.isEmpty(emp.getCountry()) || StringUtils.isEmpty(emp.getName())) {
				throw new UserException("Insertion not possible without values", "Failure");
			}
		} catch (UserException us) {
			resp.setMessage(us.getMessage());
			resp.setStatus(us.getStatus());
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
		empService.saveEmployee(emp);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(emp.getId()).toUri());
		resp.setMessage("Insertion successfull for Employee");
		resp.setStatus("successfull");
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	@DeleteMapping(value = "/deleteEmployee/{id}", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity deleteEmployee(@PathVariable("id") long id) throws UserException {
		Employee user = empService.findEmployeeById(id);
		Response resp = new Response();

		if (StringUtils.isEmpty(user)) {
			resp.setMessage("Deletion is not possible with inappropriate ID");
			resp.setStatus("Failure");
			return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
		}

		try {
			empService.deleteEmployeeById(id);
		} catch (UserException u) {
			resp.setMessage(u.getMessage());
			resp.setStatus(u.getStatus());
			return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		resp.setMessage("Deletion successfull for Employee");
		resp.setStatus("success");
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	@PutMapping(value = "/updateEmployee/{id}", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity updateEmployee(@PathVariable("id") long id, @RequestBody Employee u) {
		Response resp = new Response();
		Employee emp = empService.findEmployeeById(id);
		if (emp == null) {
			resp.setMessage("Updation cannot be done");
			resp.setStatus("Failure");
			return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);

		}

		try {
			emp.setName(u.getName());
			emp.setCountry(u.getCountry());
			if (StringUtils.isEmpty(u.getName()) || StringUtils.isEmpty(u.getCountry())) {
				throw new UserException("Updation not possible because values are empty", "Failure");
			}
		} catch (UserException us) {
			resp.setMessage(us.getMessage());
			resp.setStatus(us.getStatus());
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
		Employee c = empService.updateEmployeeDetails(emp, id);
		resp.setMessage("Updation successfull for Employee");
		resp.setStatus("successfull");
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

}
