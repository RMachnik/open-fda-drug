package com.rmachnik.drugs.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "drug_applications")
public class DrugApplication {
    @Id
    private String applicationNumber;
    private String manufacturerName;
    private String substanceName;
    @ElementCollection
    private List<String> productNumbers;

    public DrugApplication() {
    }

    public DrugApplication(String applicationNumber, String manufacturerName, String substanceName, List<String> productNumbers) {
        this.applicationNumber = applicationNumber;
        this.manufacturerName = manufacturerName;
        this.substanceName = substanceName;
        this.productNumbers = productNumbers;
    }

    public String getApplicationNumber() {
        return this.applicationNumber;
    }

    public String getManufacturerName() {
        return this.manufacturerName;
    }

    public String getSubstanceName() {
        return this.substanceName;
    }

    public List<String> getProductNumbers() {
        return this.productNumbers;
    }
}

