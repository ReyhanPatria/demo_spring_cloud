package com.example.employee;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Employee")
public class EmployeePojo {
    @ApiModelProperty(example = "1")
    private Integer id;
    
    @ApiModelProperty(example = "REY")
    private String nama;

    @ApiModelProperty(example = "Foresta")
    private String lokasi;

    @ApiModelProperty(example = "WFO")
    private String status;

    @ApiModelProperty(example = "1")
    private Integer shift;

    @JsonProperty("jam_masuk")
    @ApiModelProperty(example = "-")
    private String jamMasuk;

    @JsonProperty("jam_keluar")
    @ApiModelProperty(example = "-")
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
