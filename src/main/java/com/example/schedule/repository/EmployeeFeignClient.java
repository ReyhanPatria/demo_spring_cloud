package com.example.schedule.repository;

import com.example.schedule.model.pojo.EmployeePojo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "employeeFeignClient", url = "https://demo-project-employee.herokuapp.com/") 
public interface EmployeeFeignClient {
    @PostMapping("/employee/insert")
    public EmployeePojo insertEmployee(@RequestBody EmployeePojo employee);

    @GetMapping("/employees")
    public Iterable<EmployeePojo> getAllEmployee();

    @GetMapping(value = "employee/{id}")
    public EmployeePojo getEmployeeById(@PathVariable Integer id);
}