package com.example.employee.repository;

import com.example.employee.model.Shift;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends CrudRepository<Shift, Integer> {
    
}
