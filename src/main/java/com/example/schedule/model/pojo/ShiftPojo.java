package com.example.schedule.model.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.schedule.model.Employee;
import com.example.schedule.model.Shift;
import com.fasterxml.jackson.annotation.JsonAnyGetter;

import lombok.Data;

@Data
public class ShiftPojo {
    private Map<String, List<EmployeeDetail>> properties = new HashMap<>();

    public ShiftPojo(Shift shift) {
        String shiftId = "shift_" + shift.getId();
        List<EmployeeDetail> employeeDetailList = new ArrayList<>();
        for(Employee e: shift.getEmployeeSet()) {
            EmployeeDetail employeeDetail = new EmployeeDetail(e);
            employeeDetailList.add(employeeDetail);
        }

        this.properties = new HashMap<>();
        this.properties.put(shiftId, employeeDetailList);
    }

    @JsonAnyGetter
    public Map<String, List<EmployeeDetail>> getProperties() {
        return properties;
    }

    public class EmployeeDetail {
        private String nama;
        private String lokasi;

        public EmployeeDetail(Employee employee) {
            this.nama = employee.getNama();
            this.lokasi = employee.getLokasi();
        }

        public String getNama() {
            return nama;
        }

        public String getLokasi() {
            return lokasi;
        }
    }
}