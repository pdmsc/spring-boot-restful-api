package com.example.payroll.controller;

import com.example.payroll.exceptions.EmployeeNotFoundException;
import com.example.payroll.hateoas.EmployeeModelAssembler;
import com.example.payroll.model.Employee;
import com.example.payroll.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {
    private EmployeeModelAssembler modelAssembler = Mockito.mock(EmployeeModelAssembler.class);
    private EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

    private EmployeeController underTest;

    @BeforeEach
    private void setup(){
        this.underTest = new EmployeeController(employeeRepository, modelAssembler);
    }

    @Test
    public void getAllEmployees() throws Exception{
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

        List<Employee> actualResults = underTest.all();

        verify(employeeRepository, times(1)).findAll();
        assertEquals(actualResults.size(), expectedEmployees.size());
    }

    @Test
    public void getExistingEmployeeById() throws Exception{
        Long id = 123l;

        Employee employeeOne = new Employee();
        employeeOne.setFirstName("Pablo");
        employeeOne.setLastName("Di Meu");
        employeeOne.setRole("Engineer");
        employeeOne.setId(id);

        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.of(employeeOne));

        Employee actualResult = underTest.one(id);

        verify(employeeRepository, times(1)).findById(id);
        assertEquals(actualResult.getName(), employeeOne.getName());
    }

    @Test
    public void getInexistentEmployeeByIdThrowsException() throws Exception {
        Long id = 123l;

        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> underTest.one(id)
        );

        assertTrue(exception.getMessage().contains("Could not find Employee " + id));
    }

    @Test
    void testCreateNewEmployee() {
        Employee employeeOne = new Employee();
        employeeOne.setFirstName("Pablo");
        employeeOne.setLastName("Di Meu");
        employeeOne.setRole("Engineer");
        employeeOne.setId(123l);

        when(employeeRepository.save(employeeOne)).thenReturn(employeeOne);

        Employee savedEmployee = underTest.newEmployee(employeeOne);

        verify(employeeRepository, times(1)).save(employeeOne);
        assertEquals(savedEmployee.getName(), employeeOne.getName());
    }

}




/*
*     @PostMapping("/employees")
    public Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }*/