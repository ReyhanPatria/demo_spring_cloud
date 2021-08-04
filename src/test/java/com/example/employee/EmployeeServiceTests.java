package com.example.employee;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;
    
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository);
    }

    @Test
    void insertEmployee_ShouldSucceed_WhenSaveIsCalled() {
        // Given
        EmployeePojo employeePojo = new EmployeePojo(1, "REY", "Foresta", "WFH", null, "-", "-");
        Employee employee = employeePojo.toEntity();
        
        // When
        employeeService.insertEmployee(employee);

        // Then
        ArgumentCaptor<Employee> argumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(argumentCaptor.capture());

        Employee capturedEmployee = argumentCaptor.getValue();
        Assertions.assertThat(capturedEmployee).isEqualTo(employee);
    }

    @Test
    void getAllEmployee_ShouldSucceed_WhenFindAllIsCalled() {
        // When
        employeeService.getAllEmployee();

        // Then
        verify(employeeRepository).findAll();
    }

    @Test
    void getEmployeeById_ShouldSucceed_WhenFindByIdIsCalled() {
        // Given
        EmployeePojo employeePojo = new EmployeePojo(1, "REY", "Foresta", "WFH", null, "-", "-");
        Employee employee = employeePojo.toEntity();
        employeeRepository.save(employee);

        Integer id = 1;

        // When
        employeeService.getEmployeeById(id);

        // Then
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(employeeRepository).findById(argumentCaptor.capture());

        Integer capturedId = argumentCaptor.getValue();
        Assertions.assertThat(capturedId).isEqualTo(id);
    }

    @Test
    void getEmployeeByNama_ShouldSucceed_WhenFindByNamaIsCalled() {
        // Given
        String nama = "REY";

        EmployeePojo employeePojo = new EmployeePojo(1, nama, "Foresta", "WFH", null, "-", "-");
        Employee employee = employeePojo.toEntity();
        employeeRepository.save(employee);

        // When
        employeeService.getEmployeeByNama(nama);

        // Then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(employeeRepository).findByNama(argumentCaptor.capture());

        String capturedNama = argumentCaptor.getValue();
        Assertions.assertThat(capturedNama).isEqualTo(nama);
    }
}