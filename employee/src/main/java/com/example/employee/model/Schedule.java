package com.example.employee.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.example.employee.model.pojo.SchedulePojo;

@Entity
public class Schedule {
    public static final Integer SHIFTS_PER_SCHEDULE = 5;

    @Id
    @GeneratedValue(
        generator = "schedule_id_sequence",
        strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
        name = "schedule_id_sequence",
        sequenceName = "schedule_id_sequence",
        allocationSize = 1
    )
    private Integer id;

    @OneToMany(mappedBy = "schedule")
    private Set<Shift> shiftSet;

    public Schedule() {
        this.shiftSet = new HashSet<>();
    }

    public void assignShift(Shift shift) {
        this.shiftSet.add(shift);
    }

    public SchedulePojo toPojo() {
        return new SchedulePojo(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Shift> getShiftSet() {
        return shiftSet;
    }

    public void setShift(Set<Shift> shiftSet) {
        this.shiftSet = shiftSet;
    }
}
