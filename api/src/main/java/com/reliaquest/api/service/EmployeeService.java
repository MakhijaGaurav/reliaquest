package com.reliaquest.api.service;

import com.reliaquest.api.dto.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    @Qualifier("employee-rest-template") @Autowired
    private RestTemplate restTemplate;

    @Value("${employee-server.path.get-all-employees}")
    private String getAllEmployeesUri;

    @Value("${employee-server.path.get-employee-by-id}")
    private String getEmployeeByIdUri;

    @Value("${employee-server.path.employee}")
    private String employeeUri;

    public List<Employee> getAllEmployees() {
        HttpEntity entity = new HttpEntity(null);
        ResponseEntity<ServerResponseList> result = restTemplate.exchange(
                getAllEmployeesUri, HttpMethod.GET, entity, new ParameterizedTypeReference<ServerResponseList>() {});
        return result.getBody().getData();
    }

    public List<Employee> getAllEmployeesByNameSearch(String searchString) {
        List<Employee> allEmployees = getAllEmployees();
        List<Employee> matchedNames = allEmployees.stream()
                .filter(e -> e.getEmployee_name().contains(searchString))
                .collect(Collectors.toList());
        return matchedNames;
    }

    public Employee getEmployeeById(String id) {
        HttpEntity entity = new HttpEntity(null);
        String path = String.format(getEmployeeByIdUri, id);
        ResponseEntity<ServerResponseEmployee> result = restTemplate.exchange(
                path, HttpMethod.GET, entity, new ParameterizedTypeReference<ServerResponseEmployee>() {});
        return result.getBody().getData();
    }

    public Integer getHighestSalaryOfEmployees() {
        List<Employee> allEmployees = getAllEmployees();
        Optional<Employee> maxSalary = allEmployees.stream().max(Comparator.comparing(Employee::getEmployee_salary));
        return maxSalary.get().getEmployee_salary();
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> allEmployees = getAllEmployees();
        List<String> names = allEmployees.stream()
                .sorted(Comparator.comparing(Employee::getEmployee_salary).reversed())
                .limit(10)
                .map(Employee::getEmployee_name)
                .collect(Collectors.toList());
        return names;
    }

    public Employee createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        HttpEntity entity = new HttpEntity(createEmployeeRequest);
        ResponseEntity<ServerResponseEmployee> result = restTemplate.exchange(
                employeeUri, HttpMethod.POST, entity, new ParameterizedTypeReference<ServerResponseEmployee>() {});
        return result.getBody().getData();
    }

    public String deleteEmployeeById(String id) {
        DeleteEmployeeRequest deleteEmployeeRequest = new DeleteEmployeeRequest(id);
        HttpEntity entity = new HttpEntity(deleteEmployeeRequest);
        ResponseEntity<DeleteEmployeeResponse> result = restTemplate.exchange(
                employeeUri, HttpMethod.DELETE, entity, new ParameterizedTypeReference<DeleteEmployeeResponse>() {});
        return result.getBody().getData().toString();
    }
}
