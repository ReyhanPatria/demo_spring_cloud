package com.example.employee;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void findByNama_ShouldEmployeeFound_WhenNamaExists() {
        // Given
        String nama = "REY";
        EmployeePojo employeePojo = new EmployeePojo(nama, "Foresta", "WFH", null, "-", "-");
        employeeRepository.save(employeePojo.toEntity());

        // When
        Optional<Employee> employeeExists = employeeRepository.findByNama(nama);

        // Then
        Assertions.assertThat(employeeExists).isPresent();

        // When
        Employee employee = employeeExists.get();

        // Then
        Assertions.assertThat(employee.getNama()).isEqualTo(nama);
    }

    @Test
    public void findByNama_ShouldEmployeeNotFound_WhenNamaNotExists() {
        // Given
        String nama = "REY";

        // When
        Optional<Employee> employeeExists = employeeRepository.findByNama(nama);

        // Then
        Assertions.assertThat(employeeExists).isNotPresent();
    }
}
