package com.thoughtworks.springbootemployee.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
public class Company {
    @MongoId(FieldType.OBJECT_ID)
    private String companyId;
    private String companyName;
    private List<String> employeesId;

    public Company(String companyName, List<String> employeesId) {
        this.companyName = companyName;
        this.employeesId = employeesId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public Company() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getEmployeesId() {
        return this.employeesId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEmployeesId(List<String> employeesId) {
        this.employeesId = employeesId;
    }
}
