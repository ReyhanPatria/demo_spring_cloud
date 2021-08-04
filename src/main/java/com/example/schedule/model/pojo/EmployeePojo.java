package com.example.schedule.model.pojo;

import java.time.LocalDateTime;

import com.example.schedule.model.Employee;
import com.example.schedule.model.Shift;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeePojo {
    private Integer id;
    private String nama;
    private String lokasi;
    private String status;
    private Integer shift;

    @JsonProperty("jam_masuk")
    private String jamMasuk;

    @JsonProperty("jam_keluar")
    private String jamKeluar;

    public EmployeePojo() {
        // Default empty constructor
    }

    public EmployeePojo(Employee employee) {
        this.id = employee.getId();
        this.nama = employee.getNama();
        this.lokasi = employee.getLokasi();
        this.status = employee.getStatus();

        LocalDateTime jamMasukObject = employee.getJamMasuk();
        this.jamMasuk = (jamMasukObject == null) ? "-" : jamMasukObject.toString();

        LocalDateTime jamKeluarObject = employee.getJamKeluar();
        this.jamKeluar = (jamKeluarObject == null) ? "-" : jamKeluarObject.toString();
    
        for(Shift s: employee.getShiftSet()) {
            if(this.shift == null || this.shift < s.getId())
                this.shift = s.getId();
        }
    }

    public Employee toEntity() {
        return new Employee(this);
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

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public String getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(String jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public String getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(String jamKeluar) {
        this.jamKeluar = jamKeluar;
    }
}
