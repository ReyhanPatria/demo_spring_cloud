package com.example.schedule.model.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.schedule.model.Employee;
import com.example.schedule.model.Schedule;
import com.example.schedule.model.Shift;
import com.fasterxml.jackson.annotation.JsonAnyGetter;

import lombok.Data;

@Data
public class SchedulePojo {
    private Map<String, List<ShiftDetail>> properties = new HashMap<>();

    public SchedulePojo(Schedule schedule) {
        List<ShiftDetail> shiftDetailList = new ArrayList<>();
        for(Shift s: schedule.getShiftSet()) {
            ShiftDetail shiftDetail = new ShiftDetail(s);
            shiftDetailList.add(shiftDetail);
        }
        
        String key = "schedule";
        this.properties = new HashMap<>();
        this.properties.put(key, shiftDetailList);
    }

    @JsonAnyGetter
    public Map<String, List<ShiftDetail>> getProperties() {
        return properties;
    }

    private class ShiftDetail {
        private Map<String, List<String>> properties;
    
        public ShiftDetail(Shift shift) {
            String shiftId = "shift_" + shift.getId();
            List<String> employeeNamaList = new ArrayList<>();
            for(Employee e: shift.getEmployeeSet()) {
                employeeNamaList.add(e.getNama());
            }
    
            this.properties = new HashMap<>();
            this.properties.put(shiftId, employeeNamaList);
        }
    
        @JsonAnyGetter
        public Map<String, List<String>> getProperties() {
            return properties;
        }
    }
}
