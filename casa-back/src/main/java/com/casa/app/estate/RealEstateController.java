package com.casa.app.estate;

import com.casa.app.location.City;
import com.casa.app.user.regular_user.RegularUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/realEstate")
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @PostMapping
    public RealEstateDTO create(@RequestBody RealEstateCreateDTO estateDTO) {
        return realEstateService.create(estateDTO);
    }


    @GetMapping
    public List<RealEstateDTO> getAllByOwner() {
        return realEstateService.getAllByOwner();
    }
}
