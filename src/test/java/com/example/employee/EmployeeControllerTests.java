package com.example.employee;

import static org.mockito.Mockito.when;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.util.IterableUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void insertEmployee_ShouldSucceed_WhenJsonIsValid() throws JsonProcessingException, Exception {
        EmployeePojo employeePojo = new EmployeePojo("REY", "Foresta", "WFH", null, "-", "-");
        Employee employee = employeePojo.toEntity();

        when(employeeService.insertEmployee(employee)).thenReturn(employee);

        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/employee/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employeePojo))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue())
        )
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.nama", Matchers.is("REY"))
        );
    }

    @Test
    void insertEmployee_ShouldFail_WhenJsonIsEmpty() throws JsonProcessingException, Exception {
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/employee/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getAllEmployee_ShouldSucceed_WhenIterableIsEmpty() throws Exception{
        when(employeeService.getAllEmployee()).thenReturn(IterableUtil.iterable());

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllEmployee_ShouldSucceed_WhenIterableIsNotEmpty() throws Exception{
        Employee employee = new EmployeePojo("REY", "Foresta", "WFH", null, "-", "-").toEntity();
        when(employeeService.getAllEmployee()).thenReturn(IterableUtil.iterable(employee));

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getEmployeeById_ShouldSucceed_WhenIdIsValid() throws Exception {
        EmployeePojo employeePojo = new EmployeePojo("REY", "Foresta", "WFH", null, "-", "-");
        Integer id = 1;

        when(employeeService.getEmployeeById(id)).thenReturn(Optional.of(employeePojo.toEntity()));

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nama", Matchers.is("REY")));
    }

    @Test
    void getEmployeeById_ShouldFail_WhenIdIsNotValid() throws Exception {
        Integer id = 1;

        when(employeeService.getEmployeeById(id)).thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders
            .get("/employee/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getEmployeeByNama_ShouldSucceed_WhenNamaIsValid() throws Exception {
        String nama = "REY";
        EmployeePojo employeePojo = new EmployeePojo(nama, "Foresta", "WFH", null, "-", "-");

        when(employeeService.getEmployeeByNama(nama)).thenReturn(Optional.of(employeePojo.toEntity()));

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employee")
                .param("nama", nama)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nama", Matchers.is(nama)));
    }

    @Test
    void getEmployeeByNama_ShouldFail_WhenNamaIsNotValid() throws Exception {
        String nama = "REY";

        when(employeeService.getEmployeeByNama(nama)).thenReturn(Optional.empty());

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employee")
                .param("nama", nama)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
