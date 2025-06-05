package com.reliaquest.api;

import static com.reliaquest.api.TestFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.*;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllEmployees() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseList>>any()))
                .thenReturn(ResponseEntity.ok(returnServerResponseList()));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee"))
                .andExpect(status().isOk())
                .andReturn();
        List<Employee> employees = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Employee>>() {});

        Assertions.assertEquals(2, employees.size());
        // more validations can be added
    }

    @Test
    void testFailedToFetchAllEmployees() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseList>>any()))
                .thenThrow(new RuntimeException("Error while getting response"));

        MvcResult result = mockMvc.perform(get("/employee"))
                .andExpect(status().is5xxServerError())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Error while getting response"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee/1"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseEmployee>>any()))
                .thenReturn(ResponseEntity.ok(returnServerResponseEmployee()));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andReturn();
        Employee employee =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Employee>() {});

        Assertions.assertEquals("1", employee.getId());
        // more validations can be added
    }

    @Test
    void InvalidGetEmployeeById() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee/8f4b2c68-d3a6-4e2b-bcf0-34c8e72df87a"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseEmployee>>any()))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ServerResponseEmployee(null, "Successfully processed request.")));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/8f4b2c68-d3a6-4e2b-bcf0-34c8e72df87a"))
                .andExpect(status().is5xxServerError())
                .andReturn();

        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("Invalid Employee Id"));
    }

    @Test
    void testGetAllEmployeesBySearch() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseList>>any()))
                .thenReturn(ResponseEntity.ok(returnServerResponseList()));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/search/Test1"))
                .andExpect(status().isOk())
                .andReturn();
        List<Employee> employees = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Employee>>() {});

        Assertions.assertEquals(1, employees.size());
        Assertions.assertEquals("Test1", employees.get(0).getEmployee_name());
        // more validations can be added
    }

    @Test
    void testGetAllEmployeesByInvalidSearch() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseList>>any()))
                .thenReturn(ResponseEntity.ok(returnServerResponseList()));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/search/Gaurav"))
                .andExpect(status().isOk())
                .andReturn();
        List<Employee> employees = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Employee>>() {});

        Assertions.assertEquals(0, employees.size());
        // more validations can be added
    }

    @Test
    void testGetMaxSalaryOfEmployee() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseList>>any()))
                .thenReturn(ResponseEntity.ok(returnServerResponseList()));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/highestSalary"))
                .andExpect(status().isOk())
                .andReturn();
        Integer maxSalary =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Integer>() {});

        Assertions.assertEquals(9999, maxSalary);
        // more validations can be added
    }

    @Test
    void testTopMaxSalaryOfEmployee() throws Exception {
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseList>>any()))
                .thenReturn(ResponseEntity.ok(returnServerResponseList()));

        MvcResult mvcResult = this.mockMvc
                .perform(get("/employee/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andReturn();
        List<String> employeeNames = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<String>>() {});

        Assertions.assertEquals(2, employeeNames.size());
        // testing the order descending since limited data of 2 emp
        Assertions.assertEquals("Test2", employeeNames.get(0));
        Assertions.assertEquals("Test1", employeeNames.get(1));
        // more validations can be added
    }

    @Test
    void testSaveNewEmployee() throws Exception {
        HttpEntity entity = new HttpEntity(returnCreateEmployeeRequest());
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.POST),
                        eq(entity),
                        ArgumentMatchers.<ParameterizedTypeReference<ServerResponseEmployee>>any()))
                .thenReturn(ResponseEntity.ok(returnServerResponseEmployee()));

        MvcResult mvcResult = this.mockMvc
                .perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(returnCreateEmployeeRequest())))
                .andExpect(status().isOk())
                .andReturn();
        Employee employee =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Employee>() {});
        Assertions.assertEquals("Test1", employee.getEmployee_name());
        // more validations can be added
    }

    @Test
    void testDeleteEmployee() throws Exception {
        HttpEntity entity = new HttpEntity(returnDeleteEmployeeRequest());
        when(restTemplate.exchange(
                        eq("/api/v1/employee"),
                        eq(HttpMethod.DELETE),
                        eq(entity),
                        ArgumentMatchers.<ParameterizedTypeReference<DeleteEmployeeResponse>>any()))
                .thenReturn(ResponseEntity.ok(new DeleteEmployeeResponse(true, "Success")));

        MvcResult mvcResult = this.mockMvc
                .perform(delete("/employee/Test1"))
                .andExpect(status().isOk())
                .andReturn();
        String result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<String>() {});
        Assertions.assertEquals("true", result);
        // more validations can be added
    }
}
