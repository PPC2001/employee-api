package com.example.employeeapi.service;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.model.EmployeeRequest;
import com.example.employeeapi.model.EmployeeResponse;
import com.example.employeeapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        // Check if email already exists
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new RuntimeException("Employee with email " + employeeRequest.getEmail() + " already exists");
        }

        Employee employee = mapToEntity(employeeRequest);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Check if email is taken by another employee
        if (employeeRepository.existsByEmailAndIdNot(employeeRequest.getEmail(), id)) {
            throw new RuntimeException("Employee with email " + employeeRequest.getEmail() + " already exists");
        }

        // Update employee fields
        existingEmployee.setName(employeeRequest.getName());
        existingEmployee.setPosition(employeeRequest.getPosition());
        existingEmployee.setDepartment(employeeRequest.getDepartment());
        existingEmployee.setSalary(employeeRequest.getSalary());
        existingEmployee.setEmail(employeeRequest.getEmail());
        existingEmployee.setPhone(employeeRequest.getPhone());
        existingEmployee.setHireDate(employeeRequest.getHireDate());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapToResponse(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getTotalEmployees() {
        return employeeRepository.count();
    }

    @Override
    public Double getAverageSalary() {
        return employeeRepository.findAverageSalary();
    }

    @Override
    public BigDecimal getHighestSalary() {
        return employeeRepository.findMaxSalary();
    }

    @Override
    public Integer getDepartmentCount() {
        return employeeRepository.findEmployeeCountByDepartment().size();
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getDepartment(),
                employee.getSalary(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getHireDate(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }

    private Employee mapToEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setName(employeeRequest.getName());
        employee.setPosition(employeeRequest.getPosition());
        employee.setDepartment(employeeRequest.getDepartment());
        employee.setSalary(employeeRequest.getSalary());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPhone(employeeRequest.getPhone());
        employee.setHireDate(employeeRequest.getHireDate());
        return employee;
    }
}
