package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.estate.RealEstateRepository;
import com.casa.app.util.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealEstateRequestService {


    @Autowired
    RealEstateRequestRepository realEstateRequestRepository;
    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    EmailService emailService;

    public RealEstateRequestDTO create(RealEstate estate) {
        RealEstateRequest request = new RealEstateRequest(estate);
        request = realEstateRequestRepository.save(request);
        estate.setRequest(request);
        realEstateRepository.save(estate);
        return new RealEstateRequestDTO(request);
    }

    public void manage(RealEstateRequestDTO requestDTO) throws MessagingException, UnsupportedEncodingException {
        RealEstateRequest request = realEstateRequestRepository.getReferenceById(requestDTO.getId());
        request.setApproved(requestDTO.isApproved());
        request.setComment(requestDTO.getComment());
        emailService.sendNotificationEmail(request);
        realEstateRequestRepository.save(request);
    }

    public List<RealEstateRequestDTO> getAll() {
        List<RealEstateRequest> requests = realEstateRequestRepository.findAll();
        return requests.stream().map(RealEstateRequestDTO::new).collect(Collectors.toList());
    }

    public List<RealEstateRequestDTO> getAll(boolean approved) {
        List<RealEstateRequest> requests = realEstateRequestRepository.getAllByApproved(approved);
        return requests.stream().map(RealEstateRequestDTO::new).collect(Collectors.toList());
    }
}
