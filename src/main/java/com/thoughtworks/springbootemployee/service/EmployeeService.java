package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Page<Employee> getEmployeesPaginated(Integer page, Integer pageSize) {
        return employeeRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Employee getEmployee(String employeeId) throws EmployeeNotFoundException {
        return employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> getEmployeesByGender(String gender) {
        return employeeRepository.findAllByGender(gender);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(String employeeId, Employee employee) throws EmployeeNotFoundException {
        if (this.employeeRepository.existsById(employeeId)) {
            employee.setId(employeeId);
            return employeeRepository.save(employee);
        }
        throw new EmployeeNotFoundException();
    }

    public void deleteEmployee(String employeeId) throws EmployeeNotFoundException {
        if (this.employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
            return;
        }
        throw new EmployeeNotFoundException();

    }

    public List<Employee> getEmployeesById(List<String> employeesId) {
        return StreamSupport
                .stream(employeeRepository.findAllById(employeesId).spliterator(), false)
                .collect(Collectors.toList());
    }
}
