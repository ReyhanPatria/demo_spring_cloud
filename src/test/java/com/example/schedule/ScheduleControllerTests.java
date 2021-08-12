package com.example.schedule;

import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.schedule.controller.ScheduleController;
import com.example.schedule.model.Employee;
import com.example.schedule.model.Schedule;
import com.example.schedule.model.Shift;
import com.example.schedule.service.ScheduleService;

import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(ScheduleController.class)
@ExtendWith(RestDocumentationExtension.class)
class ScheduleControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    }

    Schedule generateDummySchedule(Integer id) {
        Integer stringId = 0;
        String[] employeeNames = {
            "SIS", "BBK", "BTS", "PPT", "DOX", "MYN", 
            "REY", "JFR", "FRY", "MVP", "LME", "EJK"
        };

        Schedule newSchedule = new Schedule();
        newSchedule.setId(id);
        
        for(Integer i = 0; i < Schedule.SHIFTS_PER_SCHEDULE; i++) {
            Shift newShift = new Shift();

            for(Integer j = 0; j < Shift.EMPLOYEES_PER_SHIFT; j++) {
                Employee newEmployee = new Employee();
                newEmployee.setNama(employeeNames[j]);

                newShift.setId(i + 1);
                newShift.assignEmployee(newEmployee);
            
                stringId++;
            }
            
            newSchedule.assignShift(newShift);
        }

        return newSchedule;
    }

    @Test
    void getAllSchedule_ShouldSucceed_WhenIterableIsEmpty() throws Exception {
        when(scheduleService.getAllSchedule()).thenReturn(IterableUtil.iterable());

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedules")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
        .andDo(MockMvcRestDocumentation.document("/schedule/get-all/empty"));
    }

    @Test
    void getAllSchedule_ShouldSucceed_WhenIterableIsNotEmpty() throws Exception {
        Schedule[] newSchedules = {
            generateDummySchedule(1), 
            generateDummySchedule(2),
            generateDummySchedule(3)
        };

        when(scheduleService.getAllSchedule()).thenReturn(
            IterableUtil.iterable(newSchedules)
        );

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedules")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
        .andDo(
            MockMvcRestDocumentation.document(
                "/schedule/get-all/not-empty",
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()))
        );
    }

    @Test
    void getScheduleById_ShouldSucceed_WhenIdIsValid() throws Exception {
        Integer id = 1;
        Schedule dummySchedule = generateDummySchedule(id);
        when(scheduleService.getScheduleById(id))
            .thenReturn(Optional.of(dummySchedule));
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedule/{id}", id)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.schedule").isArray())
        .andDo(
            MockMvcRestDocumentation.document(
                "/schedule/get-by-id/success",
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
            )
        );
    }

    @Test
    void getScheduleById_ShouldFail_WhenIdIsNotValid() throws Exception {
        Integer id = 1;
        when(scheduleService.getScheduleById(id))
            .thenReturn(Optional.empty());
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedule/{id}", id)
        )
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andDo(MockMvcRestDocumentation.document("/schedule/get-by-id/fail"));
    }

    @Test
    void getGeneratedSchedule_ShouldSucceed_WhenReturnedIsNotNull() throws Exception {
        Integer id = 1;
        Schedule dummySchedule = generateDummySchedule(id);
        when(scheduleService.generateSchedule())
            .thenReturn(dummySchedule);
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/schedule/generate")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(
            MockMvcRestDocumentation.document(
                "/schedule/generate", 
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
            )
        );
    }
}