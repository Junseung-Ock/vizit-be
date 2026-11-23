package com.vizit.vizitapi.domain.region.repository;

import com.vizit.vizitapi.domain.region.entity.LegalDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegalDistrictRepository extends JpaRepository<LegalDistrict, String> {
}
