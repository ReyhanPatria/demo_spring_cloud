package com.example.schedule.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.example.schedule.model.pojo.SchedulePojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Shift> shiftSet = new HashSet<>();

    public void assignShift(Shift shift) {
        this.shiftSet.add(shift);
    }

    public SchedulePojo toPojo() {
        return new SchedulePojo(this);
    }
}
