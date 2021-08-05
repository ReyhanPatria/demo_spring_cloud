package com.example.schedule.controller;

import com.example.schedule.model.pojo.EmployeePojo;
import com.example.schedule.repository.EmployeeFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private final EmployeeFeignClient employeeFeignClient;

    @Autowired
    public EmployeeController(EmployeeFeignClient employeeFeignClient) {
        this.employeeFeignClient = employeeFeignClient;
    }

    @GetMapping("employees")
    public ResponseEntity<Iterable<EmployeePojo>> getAllEmployees() {
        Iterable<EmployeePojo> eIterable = employeeFeignClient.getAllEmployee();
        return new ResponseEntity<>(eIterable, HttpStatus.OK);
    }

    @GetMapping("employee/{id}")
    public ResponseEntity<EmployeePojo> getEmployeeById(@PathVariable Integer id) {
        EmployeePojo employee = employeeFeignClient.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}