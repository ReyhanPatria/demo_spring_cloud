package com.example.schedule;

import static org.mockito.Mockito.when;

import com.example.schedule.controller.EmployeeController;
import com.example.schedule.model.pojo.EmployeePojo;
import com.example.schedule.repository.EmployeeFeignClient;

import org.assertj.core.util.IterableUtil;
import org.hamcrest.Matchers;
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

@WebMvcTest(EmployeeController.class)
@ExtendWith(RestDocumentationExtension.class)
class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeFeignClient employeeFeignClient;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    void getAllEmployee_ShouldSucceed_WhenIterableIsEmpty() throws Exception {
        when(employeeFeignClient.getAllEmployee())
            .thenReturn(IterableUtil.iterable());

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employees")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
        .andDo(MockMvcRestDocumentation.document("/employee/get-all/empty"));
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
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
        .andDo(
            MockMvcRestDocumentation.document(
                "/employee/get-all/not-empty",
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
            )
        );
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(id)))
        .andDo(
            MockMvcRestDocumentation.document(
                "/employee/get-by-id/success",
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
            )
        );
    }
}
