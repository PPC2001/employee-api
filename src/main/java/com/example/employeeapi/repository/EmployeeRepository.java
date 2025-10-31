package com.example.employeeapi.repository;

import com.example.employeeapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartment(String department);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
    List<Object[]> findEmployeeCountByDepartment();

    @Query("SELECT AVG(e.salary) FROM Employee e")
    Double findAverageSalary();

    @Query("SELECT MAX(e.salary) FROM Employee e")
    BigDecimal findMaxSalary();
}