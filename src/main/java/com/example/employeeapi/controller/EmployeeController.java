package com.example.employeeapi.controller;

import com.example.employeeapi.model.EmployeeRequest;
import com.example.employeeapi.model.EmployeeResponse;
import com.example.employeeapi.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Employee Management", description = "APIs for managing employee records and statistics")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Get all employees", description = "Fetches a list of all employees")
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        log.info("Fetching all employees");
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        log.debug("Retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get employee by ID", description = "Fetches an employee by their unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        log.info("Fetching employee with ID: {}", id);
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        log.debug("Employee details: {}", employee);
        return ResponseEntity.ok(employee);
    }

    @Operation(summary = "Create new employee", description = "Adds a new employee to the system")
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Creating new employee: {}", employeeRequest.getName());
        EmployeeResponse createdEmployee = employeeService.createEmployee(employeeRequest);
        log.info("Employee created successfully with ID: {}", createdEmployee.getId());
        return ResponseEntity.ok(createdEmployee);
    }

    @Operation(summary = "Update employee", description = "Updates an existing employee's details")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Updating employee with ID: {}", id);
        EmployeeResponse updatedEmployee = employeeService.updateEmployee(id, employeeRequest);
        log.info("Employee with ID {} updated successfully", id);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(summary = "Delete employee", description = "Deletes an employee by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.warn("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        log.info("Employee with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get employees by department", description = "Fetches all employees belonging to a specific department")
    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(@PathVariable String department) {
        log.info("Fetching employees in department: {}", department);
        List<EmployeeResponse> employees = employeeService.getEmployeesByDepartment(department);
        log.debug("Found {} employees in department {}", employees.size(), department);
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get employee statistics", description = "Provides summary statistics like total employees, average and highest salary")
    @GetMapping("/stats/summary")
    public ResponseEntity<Map<String, Object>> getEmployeeStats() {
        log.info("Fetching employee statistics summary");
        Map<String, Object> stats = Map.of(
                "totalEmployees", employeeService.getTotalEmployees(),
                "averageSalary", employeeService.getAverageSalary() != null ?
                        employeeService.getAverageSalary() : 0.0,
                "highestSalary", employeeService.getHighestSalary() != null ?
                        employeeService.getHighestSalary() : BigDecimal.ZERO,
                "departmentCount", employeeService.getDepartmentCount()
        );
        log.debug("Employee stats: {}", stats);
        return ResponseEntity.ok(stats);
    }
}
