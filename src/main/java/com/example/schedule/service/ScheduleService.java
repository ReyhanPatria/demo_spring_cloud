package com.example.schedule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.schedule.model.Employee;
import com.example.schedule.repository.EmployeeRepository;
import com.example.schedule.model.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.model.Shift;
import com.example.schedule.repository.ShiftRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ScheduleService(
            ScheduleRepository scheduleRepository,
            ShiftRepository shiftRepository,
            EmployeeRepository employeeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
    }

    public Iterable<Schedule> getAllSchedule() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Integer id) {
        return scheduleRepository.findById(id);
    }

    /**
     * Generate a {@code Schedule} with 5 Shifts, 
     * each {@link Shift} has 3 randomized {@link Employee}s.
     * 
     * The generated {@code Schedule} object is used to update 
     * the existing Schedule and Shifts records in the database
     * 
     * @return a Schedule object
     */
    public Schedule generateSchedule() {
        Schedule newSchedule = new Schedule();
        newSchedule = scheduleRepository.save(newSchedule);

        Iterable<Employee> employeeIterable = employeeRepository.findAll();
        List<Employee> employeeList = new ArrayList<>();
        employeeIterable.forEach(employeeList::add);
        Integer employeeCount = employeeList.size();

        for(Integer i = 0; i < Schedule.SHIFTS_PER_SCHEDULE; i++) {
            Shift newShift = new Shift();
            newShift.setSchedule(newSchedule);

            for(Integer j = 0; j < Shift.EMPLOYEES_PER_SHIFT; j++) {
                if(employeeCount > 0) {
                    int index = new Random().nextInt(employeeCount);
                    Employee selectedEmployee = employeeList.remove(index); 
                    newShift.assignEmployee(selectedEmployee);
                    employeeCount--;
                }
            }

            newSchedule.assignShift(newShift);
            shiftRepository.save(newShift);
        }

        return newSchedule;
    }
}
