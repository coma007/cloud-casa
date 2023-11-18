package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealEstateRequestRepository extends JpaRepository<RealEstateRequest, Long> {

    public List<RealEstateRequest> getAllByApproved(boolean approved);
}
