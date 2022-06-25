package com.example.payroll.controller;

import com.example.payroll.hateoas.EmployeeModelAssembler;
import com.example.payroll.model.Employee;
import com.example.payroll.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private EmployeeModelAssembler modelAssembler;

    @Test
    void testGetAllEmployees() throws Exception {
        Employee employeeOne = new Employee();
        employeeOne.setFirstName("Pablo");
        employeeOne.setLastName("Di Meu");
        employeeOne.setRole("Engineer");
        employeeOne.setId(123l);

        Employee employeeTwo = new Employee();
        employeeTwo.setFirstName("Matias");
        employeeTwo.setLastName("Ag");
        employeeTwo.setRole("Developer");
        employeeTwo.setId(412l);

        List<Employee> expectedEmployees = Arrays.asList(employeeOne, employeeTwo);
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        RequestBuilder request = MockMvcRequestBuilders.get("/employees");

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Pablo Di Meu"))
                .andExpect(jsonPath("$[1].name").value("Matias Ag"));
    }

}