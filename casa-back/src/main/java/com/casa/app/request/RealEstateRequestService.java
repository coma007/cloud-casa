package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.estate.RealEstateRepository;
import com.casa.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealEstateRequestService {


    @Autowired
    RealEstateRequestRepository realEstateRequestRepository;
    @Autowired
    RealEstateRepository realEstateRepository;

    public RealEstateRequestDTO createRequest(RealEstate estate) {

        RealEstateRequest request = new RealEstateRequest(estate);
        request = realEstateRequestRepository.save(request);
        estate.setRequest(request);
        realEstateRepository.save(estate);
        return new RealEstateRequestDTO(request);
    }

    public void manageRequest(RealEstateRequestDTO requestDTO) {
        RealEstateRequest request = realEstateRequestRepository.getById(requestDTO.getId());
        request.setApproved(requestDTO.isApproved());
        request.setComment(requestDTO.getComment());
        realEstateRequestRepository.save(request);
    }
}
