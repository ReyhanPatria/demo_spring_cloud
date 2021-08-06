package com.example.schedule.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.example.schedule.model.pojo.EmployeePojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-d'T'HH:mm";

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "employee_id_sequence"
    )
    @SequenceGenerator(
        name = "employee_id_sequence",
        sequenceName = "employee_id_sequence",
        allocationSize = 1
    )
    private Integer id;

    private String nama;

    private String lokasi;

    private String status;

    @ManyToMany(mappedBy = "employeeSet")
    @EqualsAndHashCode.Exclude
    private Set<Shift> shiftSet = new HashSet<>();

    @Column(name = "jam_masuk")
    private LocalDateTime jamMasuk;

    @Column(name = "jam_keluar")
    private LocalDateTime jamKeluar;

    public Employee(EmployeePojo employeePojo) {
        this.id = employeePojo.getId();
        this.nama = employeePojo.getNama();
        this.lokasi = employeePojo.getLokasi();
        this.status = employeePojo.getStatus();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        String jamMasukString = employeePojo.getJamMasuk();
        this.jamMasuk = (jamMasukString.equals("-")) ? null : LocalDateTime.parse(jamMasukString, formatter);

        String jamKeluarString = employeePojo.getJamKeluar();
        this.jamKeluar = (jamKeluarString.equals("-")) ? null : LocalDateTime.parse(jamKeluarString, formatter);
    }

    public EmployeePojo toPojo() {
        return new EmployeePojo(this);
    }
}
