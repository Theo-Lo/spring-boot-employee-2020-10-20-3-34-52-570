package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.exception.CompanyNotFoundException;
import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final List<Company> companies = new ArrayList<>();
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping
    public List<CompanyResponse> getCompanies() {
        List<Company> companies = this.companyService.getCompanies();
        return companies.stream().map(companyMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{companyId}")
    public CompanyResponse getCompany(@PathVariable String companyId) throws CompanyNotFoundException {
        return companyMapper.toResponse(this.companyService.getCompany(companyId));
    }

    @GetMapping("/{companyId}/employees")
    public List<EmployeeResponse> getEmployees(@PathVariable String companyId) throws CompanyNotFoundException {
        List<Employee> employeeList = companyService.getEmployeeList(companyId);
        return employeeList.stream().map(employeeMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping(params = {"page", "pageSize"})
    public Page<CompanyResponse> getEmployeesInPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        Page<Company> companies = this.companyService.getCompaniesPaginated(page, pageSize);
        return companies.map(companyMapper::toResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse createCompany(@RequestBody CompanyRequest companyUpdate) throws EmployeeNotFoundException {
        Company company = companyService.createCompany(companyMapper.toEntity(companyUpdate));
        return companyMapper.toResponse(companyService.createCompany(company));
    }

    @PutMapping("/{companyId}")
    public CompanyResponse updateCompany(@PathVariable String companyId, @RequestBody CompanyRequest companyUpdated) throws CompanyNotFoundException, EmployeeNotFoundException {
        Company company = companyService.updateCompany(companyId, companyMapper.toEntity(companyUpdated));
        return companyMapper.toResponse(companyService.createCompany(company));
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable String companyId) {
        companyService.deleteCompany(companyId);
    }
}
