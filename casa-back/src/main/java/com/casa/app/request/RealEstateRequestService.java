package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.estate.RealEstateRepository;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import com.casa.app.util.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public RealEstateRequest create(RealEstate estate) {
        RealEstateRequest request = new RealEstateRequest(estate);
        request = realEstateRequestRepository.save(request);
        estate.setRequest(request);
        realEstateRepository.save(estate);
        return request;
    }

    public void manage(RealEstateRequestDTO requestDTO) throws MessagingException, IOException {
        RealEstateRequest request = realEstateRequestRepository.getReferenceById(requestDTO.getId());
        request.setApproved(requestDTO.isApproved());
        request.setDeclined(requestDTO.isDeclined());
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

    public List<RealEstateDTO> getAllRealEstate() {
        List<RealEstateRequest> requests = realEstateRequestRepository.findAll();
        return requests.stream().map(request -> new RealEstateDTO(request.getRealEstate())).collect(Collectors.toList());
    }

    public List<RealEstateDTO> getAllUnresolvedRealEstate() {
        List<RealEstateRequest> requests = realEstateRequestRepository.getAllByApprovedAndDeclined(false, false);
        return requests.stream().map(request -> new RealEstateDTO(request.getRealEstate())).collect(Collectors.toList());
    }
}
