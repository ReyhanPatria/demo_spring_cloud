package com.example.schedule.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.schedule.model.Schedule;
import com.example.schedule.model.pojo.SchedulePojo;
import com.example.schedule.service.ScheduleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping(path = "schedules")
    public Iterable<SchedulePojo> getAllSchedules() {
        Iterable<Schedule> scheduleList = scheduleService.getAllSchedule();
        List<SchedulePojo> schedulePojoList = new ArrayList<>();
        for(Schedule s: scheduleList) {
            schedulePojoList.add(s.toPojo());
        }
        return schedulePojoList;
    }

    @GetMapping(path = "schedule/{id}")
    public ResponseEntity<SchedulePojo> getScheduleById(@PathVariable Integer id) {
        Optional<Schedule> scheduleExists = scheduleService.getScheduleById(id);
        if(!scheduleExists.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Schedule schedule = scheduleExists.get();
        return new ResponseEntity<>(schedule.toPojo(), HttpStatus.OK);
    }

    @GetMapping(path = "schedule/generate")
    public ResponseEntity<SchedulePojo> getGeneratedSchedule() {
        Schedule schedule = scheduleService.generateSchedule();

        return new ResponseEntity<>(schedule.toPojo(), HttpStatus.OK);
    }
}
