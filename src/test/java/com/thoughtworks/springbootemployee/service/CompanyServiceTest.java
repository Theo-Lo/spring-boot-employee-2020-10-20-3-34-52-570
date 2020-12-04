package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.CompanyNotFoundException;
import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @InjectMocks
    CompanyService companyService;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    EmployeeService employeeService;

    private final String companyId = "1";
    private final String companyName = "Google";

    @Test
    void should_return_all_companies_when_get_all_given_all_companies() {
        //given
        final List<Company> expected = new ArrayList<>();
        expected.add(new Company());
        expected.add(new Company());
        when(companyRepository.findAll()).thenReturn(expected);

        //when
        final List<Company> companies = companyService.getCompanies();

        //then
        assertEquals(expected, companies);
    }

    @Test
    void should_return_a_company_when_get_company_given_a_company() throws CompanyNotFoundException {
        //given
        final Company expected = new Company();
        when(companyRepository.findById(any())).thenReturn(Optional.of(expected));

        //when
        final Company company = companyService.getCompany(companyId);

        //then
        assertEquals(expected, company);
    }

    @Test
    void should_return_employee_list_when_get_a_company_employee_list_given_a_company() throws CompanyNotFoundException {
        //given
        final Company expected = new Company(companyName, new ArrayList<>());
        when(companyRepository.findById(any())).thenReturn(Optional.of(expected));

        //when
        final List<Employee> employeesList = companyService.getEmployeeList("1");

        //then
        assertEquals(new ArrayList<>(), employeesList);
    }

    @Test
    void should_return_correct_page_when_get_all_given_all_companies_and_page_with_page_size() {
        //given
        final List<Company> fullList = new ArrayList<>();
        fullList.add(new Company());
        fullList.add(new Company());
        fullList.add(new Company());

        final Page<Company> expected = new PageImpl<>(fullList);

        when(companyRepository.findAll((Pageable) any())).thenReturn(expected);
        //when
        final Page<Company> actual = companyService.getCompaniesPaginated(1, 2);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_a_company_when_create_given_a_company() throws EmployeeNotFoundException {
        //given
        final Company expected = new Company("OOCL", new ArrayList<>());
        when(companyRepository.save(any())).thenReturn(expected);

        //when
        final Company actual = companyService.createCompany(expected);

        //then
        assertEquals(expected, actual);
    }

    // use argument capture
    @Test
    void should_return_updated_company_when_update_given_a_company_id_and_company_updates() throws CompanyNotFoundException, EmployeeNotFoundException {
        //given
        final Company expected = new Company("OOCL", new ArrayList<>());
        when(companyRepository.existsById(any())).thenReturn(true);
        when(companyRepository.save(any())).thenReturn(expected);

        //when
        Company actual = companyService.updateCompany(companyId, expected);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_null_when_delete_given_a_company_id() {
        //given

        //when
        companyService.deleteCompany(companyId);

        //then
        verify(companyRepository, times(1)).deleteById(companyId);
    }
}
