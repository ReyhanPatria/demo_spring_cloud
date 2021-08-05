package com.example.schedule;

import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.schedule.controller.ScheduleController;
import com.example.schedule.model.Schedule;
import com.example.schedule.service.ScheduleService;

import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void getAllSchedule_ShouldSucceed_WhenIterableIsEmpty() throws Exception {
        when(scheduleService.getAllSchedule()).thenReturn(IterableUtil.iterable());

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedules")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void getAllSchedule_ShouldSucceed_WhenIterableIsNotEmpty() throws Exception {
        when(scheduleService.getAllSchedule()).thenReturn(
            IterableUtil.iterable(new Schedule())
        );

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedules")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void getScheduleById_ShouldSucceed_WhenIdIsValid() throws Exception {
        Integer id = 1;
        when(scheduleService.getScheduleById(id))
            .thenReturn(Optional.of(new Schedule()));
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedule/{id}", id)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.schedule").isArray());
    }

    @Test
    void getScheduleById_ShouldFail_WhenIdIsNotValid() throws Exception {
        Integer id = 1;
        when(scheduleService.getScheduleById(id))
            .thenReturn(Optional.empty());
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedule/{id}", id)
        )
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getGeneratedSchedule_ShouldSucceed_WhenReturnedIsNotNull() throws Exception {
        when(scheduleService.generateSchedule())
            .thenReturn(new Schedule());
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedule/generate")
        )
        .andExpect(MockMvcResultMatchers.status().isOk());
    }
}