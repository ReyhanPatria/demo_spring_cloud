package com.example.schedule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.example.schedule.model.Schedule;
import com.example.schedule.model.Shift;
import com.example.schedule.model.pojo.EmployeePojo;
import com.example.schedule.repository.EmployeeFeignClient;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.ShiftRepository;
import com.example.schedule.service.ScheduleService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTests {
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @Mock
    private ShiftRepository shiftRepository;
    
    @Mock
    private EmployeeFeignClient employeeFeignClient;

    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        this.scheduleService = new ScheduleService(
            scheduleRepository, 
            shiftRepository, 
            employeeFeignClient
        );
    }

    void setDummyEmployeeData() {
        Integer totalEmployee = Schedule.SHIFTS_PER_SCHEDULE * Shift.EMPLOYEES_PER_SHIFT;

        this.employeeFeignClient = mock(EmployeeFeignClient.class);
        when(employeeFeignClient.getAllEmployee()).thenAnswer(
            new Answer<Iterable<EmployeePojo>>(){
                @Override
                public Iterable<EmployeePojo> answer(InvocationOnMock arg0) throws Throwable {
                    List<EmployeePojo> eIteriable = new ArrayList<>();
                    for(Integer i = 1; i <= totalEmployee; i++) {
                        eIteriable.add(
                            new EmployeePojo(i, "REY", "Foresta", "WFH", null, "-", "-")
                        );
                    }
                    return eIteriable;
                }
            }
        );

        this.scheduleService = new ScheduleService(
            scheduleRepository, 
            shiftRepository, 
            employeeFeignClient
        );
    }
    
    @Test
    void getAllSchedule_ShouldSucceed_WhenFindAllIsCalled() {
        // When
        scheduleService.getAllSchedule();

        // Then
        verify(scheduleRepository).findAll();
    }

    @Test
    void getScheduleById_ShouldSucceed_WhenFindByIdIsCalled() {
        // Given
        Integer id = 1;
        Schedule schedule = new Schedule();
        schedule.setId(id);

        scheduleRepository.save(schedule);

        // When
        scheduleService.getScheduleById(id);

        // Then
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(scheduleRepository).findById(argumentCaptor.capture());

        Integer capturedId = argumentCaptor.getValue();
        Assertions.assertThat(capturedId).isEqualTo(id);
    }

    @Test
    void generateSchedule_ShouldSucceed_WhenNewScheduleIsReturned() {
        // When
        Schedule generatedSchedule = scheduleService.generateSchedule();

        // Then
        // Verify a new schedule object is generated
        Assertions.assertThat(generatedSchedule).isNotNull();
    }

    @Test
    void generateSchedule_ShouldSucceed_WhenNewScheduleIsSavedIntoDatabase() {
        // When
        scheduleService.generateSchedule();

        // Then
        // Verify new schedule is saved into the database
        ArgumentCaptor<Schedule> newSchedule = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(newSchedule.capture());
        Assertions.assertThat(newSchedule.getValue()).isNotNull();
    }

    @Test
    void generateSchedule_ShouldSucceed_WhenFiveShiftsWithThreeEmployeesIsSaved() {
        // Given
        setDummyEmployeeData();
        
        // When
        scheduleService.generateSchedule();

        // Then
        // Verify number of times shift is called
        ArgumentCaptor<Shift> newShift = ArgumentCaptor.forClass(Shift.class);
        verify(shiftRepository, times(Schedule.SHIFTS_PER_SCHEDULE)).save(newShift.capture());
        Assertions.assertThat(newShift.getValue()).isNotNull();
        Assertions.assertThat(newShift.getValue().getEmployeeSet())
            .hasSize(Shift.EMPLOYEES_PER_SHIFT);
    }

    @Test
    void generateSchedule_ShouldSucceed_WhenEmployeeDataIsGottenSuccessfully() {
        // Given
        Integer totalEmployee = Schedule.SHIFTS_PER_SCHEDULE * Shift.EMPLOYEES_PER_SHIFT;
        setDummyEmployeeData();

        // When
        scheduleService.generateSchedule();

        // Then
        // Verify all employees are gotten
        verify(employeeFeignClient).getAllEmployee();
        Assertions.assertThat(employeeFeignClient.getAllEmployee())
            .hasSize(totalEmployee);
    }
}
