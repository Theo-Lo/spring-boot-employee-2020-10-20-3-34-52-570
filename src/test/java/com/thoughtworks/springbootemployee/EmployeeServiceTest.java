package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmployeeServiceTest {
    @Test
    void should_return_all_employees_when_get_all_given_all_employees() {
        //given
        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        final List<Employee> expected = new ArrayList<>();
        expected.add(new Employee(1, "Marcus", 22, "male", 50));
        expected.add(new Employee(2, "Theo", 22, "male", 50000));
        expected.forEach(employeeRepository::create);

        //when
        final List<Employee> employees = employeeService.getEmployees();

        //then
        assertEquals(expected, employees);
    }

    @Test
    void should_return_an_employee_when_get_employee_given_an_employee() {
        //given
        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee expected = new Employee(1, "Marcus", 22, "male", 50);
        employeeRepository.create(expected);

        //when
        final Employee employee = employeeService.getEmployee(expected.getId());

        //then
        assertEquals(expected, employee);
    }

    @Test
    void should_return_only_male_employee_when_get_employee_given_gender_is_male() {
        //given
        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        final List<Employee> fullList = new ArrayList<>();
        fullList.add(new Employee(1, "Marcus", 22, "male", 50));
        fullList.add(new Employee(2, "Theo", 22, "male", 50000));
        fullList.add(new Employee(3, "Linne", 22, "female", 500000));
        fullList.forEach(employeeRepository::create);

        final List<Employee> expected = fullList.stream()
                .filter(expectedEmployee -> expectedEmployee.getGender().equals("male"))
                .collect(Collectors.toList());

        //when
        final List<Employee> employees = employeeService.getEmployeesByGender("male");

        //then

        assertEquals(expected, employees);
    }

    @Test
    void should_return_correct_page_when_get_employee_given_page_and_page_size() {
        //given
        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        final List<Employee> fullList = new ArrayList<>();
        fullList.add(new Employee(1, "Marcus", 22, "male", 50));
        fullList.add(new Employee(2, "Theo", 22, "male", 50000));
        fullList.add(new Employee(3, "Linne", 22, "female", 500000));
        fullList.forEach(employeeRepository::create);

        final List<Employee> expected = fullList.stream()
                .limit(2)
                .collect(Collectors.toList());

        //when
        final List<Employee> employees = employeeService.getEmployeesPaginized(1, 2);

        //then

        assertEquals(expected, employees);
    }

    @Test
    void should_return_created_employee_when_create_employee_given_an_employee() {
        //given
        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee expected = new Employee(1, "Marcus", 22, "male", 50);

        //when
        employeeService.createEmployee(expected);
        final Employee employee = employeeService.getEmployee(expected.getId());

        //then
        assertEquals(expected, employee);
    }

    @Test
    void should_return_updated_employee_when_update_employee_given_an_employee_id_and_employee() {
        //given
        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee beforeUpdateEmployee = new Employee(1, "Marcus", 22, "male", 50);
        employeeService.createEmployee(beforeUpdateEmployee);
        Employee expected = new Employee(1, "Theo", 22, "male", 5000);

        //when
        employeeService.updateEmployee(1, expected);
        final Employee employee = employeeService.getEmployee(expected.getId());

        //then
        assertEquals(expected, employee);
    }

    @Test
    void should_return_null_when_delete_employee_given_an_employee_id() {
        //given
        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee expected = new Employee(1, "Marcus", 22, "male", 50);
        employeeService.createEmployee(expected);

        //when
        employeeService.deleteEmployee(1);
        final Employee employee = employeeService.getEmployee(expected.getId());

        //then
        assertNull(employee);
    }
}