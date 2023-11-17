package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateRequestRepository extends JpaRepository<RealEstateRequest, Long> {

}
