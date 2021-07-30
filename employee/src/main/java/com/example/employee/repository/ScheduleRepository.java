package com.example.employee.repository;

import com.example.employee.model.Schedule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {
    
}
