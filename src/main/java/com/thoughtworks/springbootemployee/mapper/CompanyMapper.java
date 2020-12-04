package com.thoughtworks.springbootemployee.mapper;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    @Autowired
    private EmployeeService employeeService;

    public Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();

        BeanUtils.copyProperties(companyRequest, company);

        return company;
    }

    public CompanyResponse toResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();

        BeanUtils.copyProperties(company, companyResponse);
        companyResponse.setEmployees(employeeService.getEmployeesById(company.getEmployeesId()));
        companyResponse.setEmployeesNumber(employeeService.getEmployeesById(company.getEmployeesId()).size());

        return companyResponse;
    }
}
