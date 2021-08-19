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

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(path = "employee/insert")
    @ApiOperation(
        value = "Insert a new employee object into database",
        notes = "All attributes except shift cannot be null",
        response = EmployeePojo.class,
        consumes = "application/json",
        produces = "application/json"
    )
    public ResponseEntity<EmployeePojo> insertEmployee(@RequestBody EmployeePojo employeePojo) {
        employeeService.insertEmployee(employeePojo.toEntity());
        return new ResponseEntity<>(employeePojo, HttpStatus.OK);
    }
    
    @GetMapping(path = "/employees")
    @ApiOperation(
        value = "Get all employees data",
        notes = "Can be empty",
        response = EmployeePojo.class,
        responseContainer = "List",
        produces = "application/json"
    )
    public ResponseEntity<List<EmployeePojo>> getAllEmployee() {
        Iterable<Employee> allEmployees = employeeService.getAllEmployee();
        
        List<EmployeePojo> output = new ArrayList<>();
        for(Employee e: allEmployees) {
            output.add(e.toPojo());
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping(path = "employee/{id}")
    @ApiOperation(
        value = "Get employee data based on id",
        response = EmployeePojo.class,
        produces = "application/json"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Provided id is invalid"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No employee with provided id was found"
        )
    })
    public ResponseEntity<EmployeePojo> getEmployeeById(@PathVariable Integer id) {
        Optional<Employee> employeeExists = employeeService.getEmployeeById(id);
        if(!employeeExists.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Employee employee = employeeExists.get();
        return new ResponseEntity<>(employee.toPojo(), HttpStatus.OK);
    }

    @GetMapping(path = "employee", params = "nama")
    @ApiOperation(
        value = "Get employee data based on nama",
        response = EmployeePojo.class,
        produces = "application/json"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "No employee with provided name was found"
        )
    })
    public ResponseEntity<EmployeePojo> getEmployeeByNama(@RequestParam String nama) {
        Optional<Employee> employeeExists = employeeService.getEmployeeByNama(nama);
        if(!employeeExists.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Employee employee = employeeExists.get();
        return new ResponseEntity<>(employee.toPojo(), HttpStatus.FOUND);
    }
}