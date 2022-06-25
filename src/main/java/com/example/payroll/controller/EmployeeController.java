package com.example.payroll.controller;

import com.example.payroll.hateoas.EmployeeModelAssembler;
import com.example.payroll.exceptions.EmployeeNotFoundException;
import com.example.payroll.model.Employee;
import com.example.payroll.repository.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler employeeModelAssembler;

    EmployeeController(EmployeeRepository repository, EmployeeModelAssembler employeeModelAssembler) {
        this.repository = repository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping("/employees")
    public List<Employee> all() {
        return repository.findAll();
    }

//    @GetMapping("/employees")
//    public CollectionModel<EntityModel<Employee>> all() {
//        List<EntityModel<Employee>> employees = repository.findAll()
//                .stream()
//                .map(employeeModelAssembler::toModel)
//                .collect(Collectors.toList());
//
//        return CollectionModel.of(
//                employees,
//                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
//    }

    @PostMapping("/employees")
    public Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

//    @PostMapping("/employees")
//    public ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
//        EntityModel<Employee> entityModel = employeeModelAssembler
//                .toModel(repository.save(newEmployee));
//
//        return ResponseEntity
//                .created(entityModel
//                        .getRequiredLink(IanaLinkRelations.SELF)
//                        .toUri())
//                .body(entityModel);
//    }

    @GetMapping("/employees/{id}")
    public Employee one(@PathVariable Long id) {
        return repository.findById(id).
                orElseThrow(() -> new EmployeeNotFoundException(id));
    }

//    @GetMapping("/employees/{id}")
//    public EntityModel<Employee> one(@PathVariable Long id) {
//        Employee employee = repository.findById(id)
//                .orElseThrow(() -> new EmployeeNotFoundException(id));
//
//        return employeeModelAssembler.toModel(employee);
//    }

//    @PutMapping("/employees/{id}")
//    public Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//        return repository.findById(id)
//                .map((Employee existingEmployee) -> {
//                    existingEmployee.setName(newEmployee.getName());
//                    existingEmployee.setRole(newEmployee.getRole());
//                    return repository.save(existingEmployee);
//                })
//                .orElseGet(() -> {
//                    newEmployee.setId(id);
//                    return repository.save(new Employee());
//                });
//    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updatedEmployee = repository.findById(id)
                .map((Employee existingEmployee) -> {
                    existingEmployee.setName(newEmployee.getName());
                    existingEmployee.setRole(newEmployee.getRole());

                    return repository.save(existingEmployee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(updatedEmployee);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

//    @DeleteMapping("/employees/{id}")
//    public void deleteEmployee(@PathVariable Long id) {
//        repository.deleteById(id);
//    }
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
