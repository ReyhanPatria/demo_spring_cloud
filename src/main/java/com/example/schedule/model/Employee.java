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
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Employee {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

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
    private Set<Shift> shiftSet;

    @Column(name = "jam_masuk")
    private LocalDateTime jamMasuk;

    @Column(name = "jam_keluar")
    private LocalDateTime jamKeluar;

    public Employee() {
        this.shiftSet = new HashSet<>();
    }

    public Employee(String nama,
            String lokasi,
            String status, 
            LocalDateTime jamMasuk,
            LocalDateTime jamKeluar) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.status = status;
        this.jamMasuk = jamMasuk;
        this.jamKeluar = jamKeluar;

        this.shiftSet = new HashSet<>();
    }

    public Employee(EmployeePojo employeePojo) {
        this.nama = employeePojo.getNama();
        this.lokasi = employeePojo.getLokasi();
        this.status = employeePojo.getStatus();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        String jamMasukString = employeePojo.getJamMasuk();
        this.jamMasuk = (jamMasukString.equals("-")) ? null : LocalDateTime.parse(jamMasukString, formatter);

        String jamKeluarString = employeePojo.getJamKeluar();
        this.jamKeluar = (jamKeluarString.equals("-")) ? null : LocalDateTime.parse(jamKeluarString, formatter);
        
        this.shiftSet = new HashSet<>();
    }

    public EmployeePojo toPojo() {
        return new EmployeePojo(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @JsonBackReference
    public Set<Shift> getShiftSet() {
        return shiftSet;
    }

    public void setShiftSet(Set<Shift> shiftSet) {
        this.shiftSet = shiftSet;
    }

    public LocalDateTime getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(LocalDateTime jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public LocalDateTime getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(LocalDateTime jamKeluar) {
        this.jamKeluar = jamKeluar;
    }        
}
