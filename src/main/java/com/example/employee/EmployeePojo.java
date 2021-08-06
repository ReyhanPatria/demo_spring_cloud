package com.example.employee;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public EmployeePojo(Employee employee) {
        this.id = employee.getId();
        this.nama = employee.getNama();
        this.lokasi = employee.getLokasi();
        this.status = employee.getStatus();

        LocalDateTime jamMasukObject = employee.getJamMasuk();
        this.jamMasuk = (jamMasukObject == null) ? "-" : jamMasukObject.toString();

        LocalDateTime jamKeluarObject = employee.getJamKeluar();
        this.jamKeluar = (jamKeluarObject == null) ? "-" : jamKeluarObject.toString();
    }

    public Employee toEntity() {
        return new Employee(this);
    }
}
