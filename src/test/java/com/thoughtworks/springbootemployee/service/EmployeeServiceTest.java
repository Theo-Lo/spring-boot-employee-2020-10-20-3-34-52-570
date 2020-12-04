package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    private final String employeeId = "1";

    @Test
    void should_return_all_employees_when_get_all_given_all_employees() {
        //given
        final List<Employee> expected = new ArrayList<>();
        expected.add(new Employee());
        expected.add(new Employee());
        when(employeeRepository.findAll()).thenReturn(expected);

        //when
        final List<Employee> actual = employeeService.getEmployees();

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_an_employee_when_get_employee_given_an_employee() throws EmployeeNotFoundException {
        //given
        Employee expected = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(expected));

        //when
        final Employee actual = employeeService.getEmployee(employeeId);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_only_male_employee_when_get_employee_given_gender_is_male() {
        //given
        final List<Employee> fullList = new ArrayList<>();
        fullList.add(new Employee());
        fullList.add(new Employee());

        when(employeeRepository.findAllByGender(any())).thenReturn(fullList);

        //when
        final List<Employee> actual = employeeService.getEmployeesByGender("male");

        //then
        assertEquals(fullList, actual);
    }

    @Test
    void should_return_correct_page_when_get_employee_given_page_and_page_size() {
        //given
        final List<Employee> fullList = new ArrayList<>();
        fullList.add(new Employee());
        fullList.add(new Employee());
        fullList.add(new Employee());

        final Page<Employee> expected = new PageImpl<>(fullList);

        when(employeeRepository.findAll((Pageable) any())).thenReturn(expected);
        //when
        final Page<Employee> actual = employeeService.getEmployeesPaginated(1, 3);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_created_employee_when_create_employee_given_an_employee() {
        //given
        Employee expected = new Employee();
        when(employeeRepository.save(any())).thenReturn(expected);

        //when
        final Employee actual = employeeService.createEmployee(expected);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_updated_employee_when_update_employee_given_an_employee_id_and_employee() throws EmployeeNotFoundException {
        //given
        Employee expected = new Employee();
        when(employeeRepository.existsById(any())).thenReturn(true);
        when(employeeRepository.save(any())).thenReturn(expected);

        //when
        Employee actual = employeeService.updateEmployee(employeeId, expected);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_call_repository_delete_by_id_when_delete_employee_given_an_employee_id() throws EmployeeNotFoundException {
        //given
        when(employeeRepository.existsById(any())).thenReturn(true);

        //when
        employeeService.deleteEmployee(employeeId);

        //then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
