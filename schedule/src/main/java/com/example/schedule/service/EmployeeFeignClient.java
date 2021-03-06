package com.example.schedule.service;

import com.example.schedule.model.pojo.EmployeePojo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employeeFeignClient", url = "http://localhost:8000") 
public interface EmployeeFeignClient {
    @GetMapping(value = "employee/{id}")
    public EmployeePojo getEmployeeById(@PathVariable Integer id);
}