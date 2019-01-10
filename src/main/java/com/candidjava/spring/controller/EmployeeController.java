ppackage com.candidjava.spring.controller;

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
	Employee employee;
	Response resp;
	private long id;

	@GetMapping(value = "/getAllEmployees", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity getEmployees() {
		List<Employee> employees = empService.getAllEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping(value = "/getEmployeeById/{id}", headers = "Accept=application/json", produces = "application/json")
	public Employee getEmployeeById(@PathVariable("id") long id) {
		Employee employee = empService.findEmployeeById(id);
		return employee;
	}

	@PostMapping(value = "/insertEmployee", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity createUser(@RequestBody Employee employee, UriComponentsBuilder ucBuilder) {
		Response response = new Response();
		try {
			if (StringUtils.isEmpty(employee.getCountry()) || StringUtils.isEmpty(employee.getName())) {
				throw new UserException("Insertion not possible without values", "Failure");
			}
		} catch (UserException us) {
			response.setMessage(us.getMessage());
			response.setStatus(us.getStatus());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		empService.saveEmployee(employee);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(employee.getId()).toUri());
		resp.setMessage("Insertion successfull for Employee");
		resp.setStatus("successfull");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@DeleteMapping(value = "/deleteEmployee/{id}", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity deleteEmployee(@PathVariable("id") long id) throws UserException {
		Employee employee = empService.findEmployeeById(id);
		Response response = new Response();

		if (StringUtils.isEmpty(employee)) {
			response.setMessage("Deletion is not possible with inappropriate ID");
			response.setStatus("Failure");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			empService.deleteEmployeeById(id);
		} catch (UserException ux) {
			response.setMessage(ux.getMessage());
			response.setStatus(ux.getStatus());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.setMessage("Deletion successfull for Employee");
		response.setStatus("success");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping(value = "/updateEmployee/{id}", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity updateEmployee(@PathVariable("id") long id, @RequestBody Employee emp) {
		Response response = new Response();
		Employee employee = empService.findEmployeeById(id);
		if (employee == null) {
			response.setMessage("Updation cannot be done");
			response.setStatus("Failure");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

		}

		try {
			employee.setName(employee.getName());
			employee.setCountry(employee.getCountry());
			if (StringUtils.isEmpty(employee.getName()) || StringUtils.isEmpty(employee.getCountry())) {
				throw new UserException("Updation not possible because values are empty", "Failure");
			}
		} catch (UserException ux) {
			response.setMessage(ux.getMessage());
			response.setStatus(ux.getStatus());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		Employee employe = empService.updateEmployeeDetails(emp, id);
		response.setMessage("Updation successfull for Employee");
		response.setStatus("successfull");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
