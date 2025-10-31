package com.example.employeeapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String position;
    private String department;
    private BigDecimal salary;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
