package com.example.schedule;

import static org.mockito.Mockito.when;

import com.example.schedule.controller.EmployeeController;
import com.example.schedule.model.pojo.EmployeePojo;
import com.example.schedule.repository.EmployeeFeignClient;

import org.assertj.core.util.IterableUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeFeignClient employeeFeignClient;

    @Test
    void getAllEmployee_ShouldSucceed_WhenIterableIsEmpty() throws Exception {
        when(employeeFeignClient.getAllEmployee())
            .thenReturn(IterableUtil.iterable());

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employees")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void getAllEmployee_ShouldSucceed_WhenIterableIsNotEmpty() throws Exception {
        EmployeePojo ePojo = new EmployeePojo(1, "REY", "Foresta", "WFH", null, "-", "-");
        when(employeeFeignClient.getAllEmployee())
            .thenReturn(IterableUtil.iterable(ePojo));

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employees")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void getEmployeeById_ShouldSucceed_WhenIdIsValid() throws Exception {
        Integer id = 1;
        EmployeePojo ePojo = new EmployeePojo(id, "REY", "Foresta", "WFH", null, "-", "-");
        when(employeeFeignClient.getEmployeeById(id))
            .thenReturn(ePojo);

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employee/{id}", id)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(id)));
    }
}
