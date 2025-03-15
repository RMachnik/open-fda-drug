package com.rmachnik.drugs.domain.repository;

import com.rmachnik.drugs.domain.DrugApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugApplicationRepository extends JpaRepository<DrugApplication, String> {

}
