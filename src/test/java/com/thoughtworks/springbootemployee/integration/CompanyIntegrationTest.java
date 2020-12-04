package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void tearDownCompanyAndEmployee() {
        companyRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    public static final String COMPANIES_URI = "/companies/";

    @Test
    void should_return_all_companies_when_get_all_given_companies() throws Exception {
        //given
        Company company = new Company("Facebook", new ArrayList<>());
        companyRepository.save(company);

        //when
        //then
        mockMvc.perform(get(COMPANIES_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].companyName").value("Facebook"))
                .andExpect(jsonPath("$[0].employeesNumber").value(0))
                .andExpect(jsonPath("$[0].employees").value(new ArrayList()));
    }

    @Test
    void should_return_a_company_when_get_company_by_id_given_company_id() throws Exception {
        //given
        Company company = companyRepository.save(new Company("Facebook", new ArrayList<>()));

        //when
        //then
        mockMvc.perform(get(COMPANIES_URI + company.getCompanyId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Facebook"))
                .andExpect(jsonPath("$.employeesNumber").value(0))
                .andExpect(jsonPath("$.employees").value(new ArrayList<>()));
    }

    @Test
    void should_return_404_when_get_company_by_id_given_wrong_company_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get(COMPANIES_URI + new ObjectId().toString()))
                .andExpect(status().isNotFound());
    }
    @Test
    void should_return_400_when_get_company_by_id_given_invalid_company_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get(COMPANIES_URI + "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_an_employee_list_when_get_employee_list_given_company_id() throws Exception {
        //given
        Employee employee1 = employeeRepository.save(new Employee("Theo", 18, "male", 50000));
        Employee employee2 = employeeRepository.save(new Employee("Linne", 18, "female", 50000));
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employee1.getId());
        employeeIdList.add(employee2.getId());
        Company company = companyRepository.save(new Company("Facebook", employeeIdList));

        //when
        //then
        mockMvc.perform(get(COMPANIES_URI + company.getCompanyId() + "/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(employee1.getId()))
                .andExpect(jsonPath("$[1].id").value(employee2.getId()));
    }

    @Test
    void should_return_404_when_get_employee_list_given_wrong_company_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get(COMPANIES_URI + new ObjectId().toString() + "/employees"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_400_when_get_employee_list_given_invalid_company_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get(COMPANIES_URI + "123" + "/employees"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_correct_page_when_get_all_given_companies_and_page_and_page_size() throws Exception {
        //given
        companyRepository.save(new Company("Facebook", new ArrayList<>()));
        companyRepository.save(new Company("Google", new ArrayList<>()));
        companyRepository.save(new Company("Apple", new ArrayList<>()));

        //when
        //then
        mockMvc.perform(get(COMPANIES_URI).param("page", "1").param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.content[0].companyName").value("Facebook"))
                .andExpect(jsonPath("$.content[0].employeesNumber").value(0))
                .andExpect(jsonPath("$.content[0].employees").value(new ArrayList<>()))
                .andExpect(jsonPath("$.content[1].companyName").value("Google"))
                .andExpect(jsonPath("$.content[1].employeesNumber").value(0))
                .andExpect(jsonPath("$.content[1].employees").value(new ArrayList<>()));
    }

    @Test
    void should_return_company_when_create_given_company() throws Exception {
        //given
        Employee employee1 = employeeRepository.save(new Employee("Theo", 18, "male", 50000));
        Employee employee2 = employeeRepository.save(new Employee("Linne", 18, "female", 50000));
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employee1.getId());
        employeeIdList.add(employee2.getId());
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        String companyAsJson = "{\n" +
                "    \"companyName\": \"OOCL\",\n" +
                "    \"employeesId\": [\""+employee1.getId()+"\", \""+employee2.getId()+"\"]\n" +
                "}";

        //when
        //then
        mockMvc.perform(post(COMPANIES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyId").isString())
                .andExpect(jsonPath("$.companyName").value("OOCL"))
                .andExpect(jsonPath("$.employeesNumber").value(employeeIdList.size()))
                .andExpect(jsonPath("$.employees[0].id").value(employeeList.get(0).getId()))
                .andExpect(jsonPath("$.employees[1].id").value(employeeList.get(1).getId()));

        List<Company> companies = companyRepository.findAll();
        assertEquals(1, companies.size());
        assertEquals("OOCL", companies.get(0).getCompanyName());
        assertEquals(employeeIdList, companies.get(0).getEmployeesId());
    }

    @Test
    void should_return_404_when_create_given_company_with_wrong_employee_id() throws Exception {
        //given
        String companyAsJson = "{\n" +
                "    \"companyName\": \"OOCL\",\n" +
                "    \"employeesId\": [\""+new ObjectId().toString()+"\", \""+ new ObjectId().toString()+"\"]\n" +
                "}";
        //when
        //then
        mockMvc.perform(post(COMPANIES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_updated_company_when_update_given_company_id() throws Exception {
        //given
        Employee employee1 = employeeRepository.save(new Employee("Theo", 18, "male", 50000));
        Employee employee2 = employeeRepository.save(new Employee("Linne", 18, "female", 50000));
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employee1.getId());
        employeeIdList.add(employee2.getId());
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        Company company = companyRepository.save(new Company("Facebook", employeeIdList));
        String companyAsJson = "{\n" +
                "    \"companyName\": \"OOCL\",\n" +
                "    \"employeesId\": [\""+employee1.getId()+"\"]\n" +
                "}";
        employeeIdList.remove(employee2.getId());

        //when
        //then
        mockMvc.perform(put(COMPANIES_URI + company.getCompanyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").isString())
                .andExpect(jsonPath("$.companyName").value("OOCL"))
                .andExpect(jsonPath("$.employeesNumber").value(employeeIdList.size()))
                .andExpect(jsonPath("$.employees[0].id").value(employeeList.get(0).getId()));

        List<Company> companies = companyRepository.findAll();
        assertEquals(1, companies.size());
        assertEquals("OOCL", companies.get(0).getCompanyName());
        assertEquals(employeeIdList, companies.get(0).getEmployeesId());
    }

    @Test
    void should_return_404_when_update_given_company_with_wrong_company_id() throws Exception {
        //given
        Employee employee1 = employeeRepository.save(new Employee("Theo", 18, "male", 50000));
        Employee employee2 = employeeRepository.save(new Employee("Linne", 18, "female", 50000));
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employee1.getId());
        employeeIdList.add(employee2.getId());
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        Company company = companyRepository.save(new Company("Facebook", employeeIdList));
        String companyAsJson = "{\n" +
                "    \"companyName\": \"OOCL\",\n" +
                "    \"employeesId\": [\""+employee1.getId()+"\"]\n" +
                "}";
        employeeIdList.remove(employee2.getId());

        //when
        //then
        mockMvc.perform(put(COMPANIES_URI + new ObjectId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_400_when_update_given_company_with_invalid_company_id() throws Exception {
        //given
        Employee employee1 = employeeRepository.save(new Employee("Theo", 18, "male", 50000));
        Employee employee2 = employeeRepository.save(new Employee("Linne", 18, "female", 50000));
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employee1.getId());
        employeeIdList.add(employee2.getId());
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        Company company = companyRepository.save(new Company("Facebook", employeeIdList));
        String companyAsJson = "{\n" +
                "    \"companyName\": \"OOCL\",\n" +
                "    \"employeesId\": [\""+employee1.getId()+"\"]\n" +
                "}";
        employeeIdList.remove(employee2.getId());

        //when
        //then
        mockMvc.perform(put(COMPANIES_URI + "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_404_when_update_given_company_with_wrong_employee_id() throws Exception {
        //given
        Employee employee1 = employeeRepository.save(new Employee("Theo", 18, "male", 50000));
        Employee employee2 = employeeRepository.save(new Employee("Linne", 18, "female", 50000));
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employee1.getId());
        employeeIdList.add(employee2.getId());
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        Company company = companyRepository.save(new Company("Facebook", employeeIdList));
        String companyAsJson = "{\n" +
                "    \"companyName\": \"OOCL\",\n" +
                "    \"employeesId\": [\""+new ObjectId().toString()+"\"]\n" +
                "}";
        employeeIdList.remove(employee2.getId());

        //when
        //then
        mockMvc.perform(put(COMPANIES_URI + company.getCompanyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_no_content_when_delete_given_company_id() throws Exception {
        //given
        Company company = companyRepository.save(new Company("Facebook", new ArrayList<>()));

        //when
        //then
        mockMvc.perform(delete(COMPANIES_URI + company.getCompanyId()))
                .andExpect(status().isNoContent());

        List<Company> companies = companyRepository.findAll();
        assertEquals(0, companies.size());
    }

    @Test
    void should_return_404_when_delete_given_company_id_with_wrong_company_id() throws Exception {
        //given

        //when
        //then
        mockMvc.perform(delete(COMPANIES_URI + new ObjectId().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_400_when_delete_given_company_id_with_invalid_company_id() throws Exception {
        //given

        //when
        //then
        mockMvc.perform(delete(COMPANIES_URI + "123"))
                .andExpect(status().isBadRequest());
    }
}
