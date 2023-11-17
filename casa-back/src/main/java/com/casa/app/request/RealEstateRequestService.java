package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import com.casa.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealEstateRequestService {


    @Autowired
    RealEstateRequestRepository realEstateRequestRepository;

    public RealEstateRequestDTO createRequest(User user, RealEstate estate) {

        RealEstateRequest request = new RealEstateRequest(estate);
        request = realEstateRequestRepository.save(request);

        return new RealEstateRequestDTO(request, user);
    }
}
