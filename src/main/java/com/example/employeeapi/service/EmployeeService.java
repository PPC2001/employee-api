package com.example.employeeapi.service;

import com.example.employeeapi.model.EmployeeRequest;
import com.example.employeeapi.model.EmployeeResponse;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest);
    void deleteEmployee(Long id);
    List<EmployeeResponse> getEmployeesByDepartment(String department);
    Long getTotalEmployees();
    Double getAverageSalary();
    BigDecimal getHighestSalary();
    Integer getDepartmentCount();
}