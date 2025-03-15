package com.rmachnik.drugs.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "drug_applications")
public class DrugApplication {
    @Id
    private String applicationNumber;
    private String manufacturerName;
    private String substanceName;
    @ElementCollection
    private List<String> productNumbers;
}

