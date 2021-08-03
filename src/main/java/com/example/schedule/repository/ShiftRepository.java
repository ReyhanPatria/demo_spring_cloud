package com.example.schedule.repository;

import com.example.schedule.model.Shift;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends CrudRepository<Shift, Integer> {
    
}
