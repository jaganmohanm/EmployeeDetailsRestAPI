package com.candidjava.spring.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.Assert.assertEquals;
import org.springframework.web.context.WebApplicationContext;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.candidjava.spring.bean.Employee;
import com.candidjava.spring.controller.EmployeeController;
import com.candidjava.spring.repository.EmployeeRepository;
import com.candidjava.spring.service.EmployeeServiceImp;

@RunWith(SpringRunner.class)
@WebMvcTest(value = EmployeeController.class, secure = false)
public class EmployeeControllerTest{

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeServiceImp employeeService;

	TestData testData=new TestData();
	Employee employee = new Employee(108, "USA", "meghana");

	@Test
	public void retrieveEmployeeDetailsByIdTest() throws Exception {

		Mockito.when(employeeService.findEmployeeById(Mockito.anyLong())).thenReturn(employee);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/getEmployeeById/108")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse());
		String expected = "{id:108,country:USA,name:meghana}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

		Assert.assertNotNull(result.getResponse().getContentAsString());
	}


	@Test
	public void insertEmployeeTest() throws Exception {
		Employee employee = new Employee(119, "Germany", "shanon");

		Mockito.when(employeeService.saveEmployee(employee)).thenReturn(true);
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee/insertEmployee/")
				.accept(MediaType.APPLICATION_JSON).content(testData.insert).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		System.out.println("Insert: "+employee.getId()+employee.getCountry()+employee.getName());
		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	
	@Test
	public void deleteEmployeeTest() throws Exception {
		Employee employee = new Employee(119, "Germany", "shanon");

		Mockito.when(employeeService.deleteEmployeeById(119)).thenReturn(employee);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employee/deleteEmployee/{id}", "119")

				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

	}
	

	@Test
	public void updateEmployeeTest() throws Exception {
		Employee employee = new Employee(118, "Germany", "shanon");

		Mockito.when(employeeService.findEmployeeById(118)).thenReturn(employee);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/updateEmployee/{id}", "118")

				.contentType(MediaType.APPLICATION_JSON).content(testData.update).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		System.out.println("Update: "+employee.getId()+employee.getCountry()+employee.getName());
		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

}











