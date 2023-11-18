package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateRepository;
import com.casa.app.util.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class RealEstateRequestService {


    @Autowired
    RealEstateRequestRepository realEstateRequestRepository;
    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    EmailService emailService;

    public RealEstateRequestDTO createRequest(RealEstate estate) {
        RealEstateRequest request = new RealEstateRequest(estate);
        request = realEstateRequestRepository.save(request);
        estate.setRequest(request);
        realEstateRepository.save(estate);
        return new RealEstateRequestDTO(request);
    }

    public void manageRequest(RealEstateRequestDTO requestDTO) throws MessagingException, UnsupportedEncodingException {
        RealEstateRequest request = realEstateRequestRepository.getById(requestDTO.getId());
        request.setApproved(requestDTO.isApproved());
        request.setComment(requestDTO.getComment());
        emailService.sendNotificationEmail(request);
        realEstateRequestRepository.save(request);
    }
}
