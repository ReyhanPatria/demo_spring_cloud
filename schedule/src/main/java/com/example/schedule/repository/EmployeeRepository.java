package com.example.schedule.repository;

import java.util.Optional;

import com.example.schedule.model.Employee;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    public Optional<Employee> findByNama(String nama);
}