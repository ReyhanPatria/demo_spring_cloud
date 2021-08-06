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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Employee> employeeSet = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    @JsonIgnore
    private Schedule schedule;

    public Shift(Schedule schedule) {
        this.schedule = schedule;
    }

    public void assignEmployee(Employee employee) {
        this.employeeSet.add(employee);
    }

    public ShiftPojo toPojo() {
        return new ShiftPojo(this);
    }
}
