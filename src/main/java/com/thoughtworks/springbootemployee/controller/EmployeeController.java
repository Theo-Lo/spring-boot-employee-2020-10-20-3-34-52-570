package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();

    @GetMapping
    public List<Employee> getAll(){
        return employees;
    }

    @PostMapping
    public Employee create(@RequestBody Employee employeeUpdate){
        employees.add(employeeUpdate);
        return employeeUpdate;
    }
}
