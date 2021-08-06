package com.example.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(path = "employee/insert")
    public ResponseEntity<EmployeePojo> insertEmployee(@RequestBody EmployeePojo employeePojo) {
        employeeService.insertEmployee(employeePojo.toEntity());
        return new ResponseEntity<>(employeePojo, HttpStatus.OK);
    }

    @GetMapping(path = "/employees")
    public ResponseEntity<Iterable<EmployeePojo>> getAllEmployee() {
        Iterable<Employee> allEmployees = employeeService.getAllEmployee();
        
        List<EmployeePojo> output = new ArrayList<>();
        for(Employee e: allEmployees) {
            output.add(e.toPojo());
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping(path = "employee/{id}")
    public ResponseEntity<EmployeePojo> getEmployeeById(@PathVariable Integer id) {
        Optional<Employee> employeeExists = employeeService.getEmployeeById(id);
        if(!employeeExists.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Employee employee = employeeExists.get();
        return new ResponseEntity<>(employee.toPojo(), HttpStatus.OK);
    }

    @GetMapping(path = "employee", params = "nama")
    public ResponseEntity<EmployeePojo> getEmployeeByNama(@RequestParam String nama) {
        Optional<Employee> employeeExists = employeeService.getEmployeeByNama(nama);
        if(!employeeExists.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Employee employee = employeeExists.get();
        return new ResponseEntity<>(employee.toPojo(), HttpStatus.FOUND);
    }
}