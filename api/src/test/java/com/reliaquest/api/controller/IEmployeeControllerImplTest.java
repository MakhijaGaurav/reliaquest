package com.reliaquest.api.controller;

import static com.reliaquest.api.TestFixtures.returnCreateEmployeeRequest;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.TestFixtures;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class IEmployeeControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllEmployees() throws Exception {
        // given
        when(employeeService.getAllEmployees()).thenReturn(TestFixtures.getAllEmployees());
        // when
        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee"))
                .andExpect(status().isOk())
                .andReturn();
        List<Employee> employees = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Employee>>() {});
        // then
        Assertions.assertEquals(2, employees.size());
        Assertions.assertEquals("Test1", employees.get(0).getEmployee_name());
        Assertions.assertEquals("1", employees.get(0).getId());
        Assertions.assertEquals("Test2", employees.get(1).getEmployee_name());
        Assertions.assertEquals("2", employees.get(1).getId());
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {
        // given
        when(employeeService.getAllEmployeesByNameSearch("Test")).thenReturn(TestFixtures.getAllEmployees());
        // when
        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/search/Test"))
                .andExpect(status().isOk())
                .andReturn();
        List<Employee> employees = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Employee>>() {});
        // then
        Assertions.assertEquals(2, employees.size());
    }

    @Test
    void getEmployeeById() throws Exception {
        // given
        when(employeeService.getEmployeeById("1"))
                .thenReturn(TestFixtures.getAllEmployees().get(0));
        // when
        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andReturn();
        Employee employee =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Employee>() {});
        // then
        Assertions.assertEquals("1", employee.getId());
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(9999);
        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/highestSalary"))
                .andExpect(status().isOk())
                .andReturn();
        Integer max =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Integer>() {});
        // then
        Assertions.assertEquals(9999, max);
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        List<Employee> allEmployees = TestFixtures.getAllEmployees();
        // given
        List<String> names =
                allEmployees.stream().map(Employee::getEmployee_name).collect(Collectors.toList());
        // when
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(names);
        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andReturn();
        List<String> namesOfEmployees = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<String>>() {});
        // then
        Assertions.assertEquals(2, namesOfEmployees.size());
    }

    @Test
    void createEmployee() throws Exception {
        when(employeeService.createEmployee(returnCreateEmployeeRequest()))
                .thenReturn(TestFixtures.getAllEmployees().get(0));
        MvcResult mvcResult = this.mockMvc
                .perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(returnCreateEmployeeRequest())))
                .andExpect(status().isOk())
                .andReturn();
        Employee employee =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Employee>() {});
        Assertions.assertEquals("1", employee.getId());
        Assertions.assertEquals("Test1", employee.getEmployee_name());
    }

    @Test
    void deleteEmployeeById() throws Exception {
        when(employeeService.deleteEmployeeById("Test1")).thenReturn("true");
        MvcResult mvcResult = this.mockMvc
                .perform(delete("/employee/Test1"))
                .andExpect(status().isOk())
                .andReturn();
        String result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<String>() {});
        Assertions.assertEquals("true", result);
    }
}
