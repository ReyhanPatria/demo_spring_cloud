package com.example.schedule.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.example.schedule.model.pojo.ShiftPojo;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Shift {
    public static final Integer EMPLOYEES_PER_SHIFT = 3; 
    
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "shift_id_sequence"
    )
    @SequenceGenerator(
        name = "shift_id_sequence",
        sequenceName = "shift_id_sequence",
        allocationSize = 1
    )
    private Integer id;

    @ManyToMany
    @JoinTable(
        name = "shift_employee",
        joinColumns = @JoinColumn(
            name = "shift_id", 
            referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "employee_id", 
            referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Employee> employeeSet;

    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    @JsonIgnore
    private Schedule schedule;

    public Shift() {
        this.employeeSet = new HashSet<>();
    }

    public Shift(Schedule schedule) {
        this.schedule = schedule;
        this.employeeSet = new HashSet<>();
    }

    public void assignEmployee(Employee employee) {
        this.employeeSet.add(employee);
    }

    public ShiftPojo toPojo() {
        return new ShiftPojo(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Employee> getEmployeeSet() {
        return employeeSet;
    }

    public void setEmployeSet(Set<Employee> employeeSet) {
        this.employeeSet = employeeSet;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
