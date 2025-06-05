package com.reliaquest.api.controller;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.DeleteEmployeeRequest;
import com.reliaquest.api.dto.Employee;
import java.util.List;

public class TestFixtures {

    public static List<Employee> getAllEmployees() {
        Employee employee1 = new Employee();
        employee1.setId("1");
        employee1.setEmployee_name("Test1");
        employee1.setEmployee_email("test1@email.com");
        employee1.setEmployee_age(20);
        employee1.setEmployee_salary(8888);
        employee1.setEmployee_title("Engineer");

        Employee employee2 = new Employee();
        employee2.setId("2");
        employee2.setEmployee_name("Test2");
        employee2.setEmployee_email("test2@email.com");
        employee2.setEmployee_age(30);
        employee2.setEmployee_salary(9999);
        employee2.setEmployee_title("Doctor");
        return List.of(employee1, employee2);
    }

    public static CreateEmployeeRequest returnCreateEmployeeRequest() {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setName("Test1");
        createEmployeeRequest.setAge(20);
        createEmployeeRequest.setSalary(8888);
        createEmployeeRequest.setTitle("Engineer");
        return createEmployeeRequest;
    }

    public static DeleteEmployeeRequest returnDeleteEmployeeRequest() {
        DeleteEmployeeRequest deleteEmployeeRequest = new DeleteEmployeeRequest("Test1");
        return deleteEmployeeRequest;
    }
}
