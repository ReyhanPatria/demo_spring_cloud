package com.example.employee;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.util.IterableUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestDocumentation;
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

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EmployeeService employeeService;

    // Build Mock MVC to have documentaion provider
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    }

    ResponseFieldsSnippet getEmployeePojoResponseFieldSnippet() {
        return PayloadDocumentation.responseFields(
            fieldWithPath("id").description("Employee's id"),
            fieldWithPath("nama").description("Employee's name"),
            fieldWithPath("lokasi").description("Employee's office location"),
            fieldWithPath("status").description("Employee's current workplace"),
            fieldWithPath("shift").description("Employee's shift on latest schedule"),
            fieldWithPath("jam_masuk").description("Employee's starting work hour"),
            fieldWithPath("jam_keluar").description("Employee's ending work hour")
        );
    }

    // Documented tests
    @Test
    void insertEmployee_ShouldSucceed_WhenJsonIsValid() throws JsonProcessingException, Exception {
        EmployeePojo employeePojo = new EmployeePojo(1, "REY", "Foresta", "WFH", 1, "-", "-");
        Employee employee = employeePojo.toEntity();

        when(employeeService.insertEmployee(employee)).thenReturn(employee);

        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/employee/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employeePojo))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nama", Matchers.is("REY")))
        // Generate snippets
        .andDo(
            MockMvcRestDocumentation.document(
                "/post/insert", 
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                getEmployeePojoResponseFieldSnippet()
            )
        );
    }

    @Test
    void getAllEmployee_ShouldSucceed_WhenIterableIsNotEmpty() throws Exception{
        Employee employee1 = new EmployeePojo(1, "REY", "Foresta", "WFH", 1, "-", "-").toEntity();
        Employee employee2 = new EmployeePojo(2, "CBT", "Foresta", "WFH", 1, "-", "-").toEntity();
        when(employeeService.getAllEmployee()).thenReturn(IterableUtil.iterable(employee1, employee2));

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        // Generate snippets
        .andDo(
            MockMvcRestDocumentation.document(
                "/get/all", 
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
            )
        );
    }

    @Test
    void getEmployeeById_ShouldSucceed_WhenIdIsValid() throws Exception {
        Integer id = 1;
        EmployeePojo employeePojo = new EmployeePojo(id, "REY", "Foresta", "WFH", 1, "-", "-");
        
        when(employeeService.getEmployeeById(id)).thenReturn(Optional.of(employeePojo.toEntity()));

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/employee/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(id)))
        // Generate snippets
        .andDo(
            MockMvcRestDocumentation.document(
                "/get/by-id", 
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                RequestDocumentation.pathParameters(
                    parameterWithName("id").description("Id of searched employee")
                ),
                getEmployeePojoResponseFieldSnippet()
            )
        );
    }

    @Test
    void getEmployeeByNama_ShouldSucceed_WhenNamaIsValid() throws Exception {
        String nama = "REY";
        EmployeePojo employeePojo = new EmployeePojo(1, nama, "Foresta", "WFH", 1, "-", "-");

        when(employeeService.getEmployeeByNama(nama)).thenReturn(Optional.of(employeePojo.toEntity()));

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employee")
                .param("nama", nama)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nama", Matchers.is(nama)))
        // Generate snippets
        .andDo(
            MockMvcRestDocumentation.document(
                "/get/by-name",
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                RequestDocumentation.requestParameters(
                    parameterWithName("nama").description("Name of searched employee")
                ),
                getEmployeePojoResponseFieldSnippet()
            )
        );
    }

    // Undocumented tests
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
    void getEmployeeById_ShouldFail_WhenIdIsNotValid() throws Exception {
        Integer id = 1;

        when(employeeService.getEmployeeById(id)).thenReturn(Optional.empty());

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/employee/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
        );
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
