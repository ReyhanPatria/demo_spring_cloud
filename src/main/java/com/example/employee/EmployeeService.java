package com.example.employee;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee insertEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Iterable<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByNama(String nama) {
        return employeeRepository.findByNama(nama);
    }
}