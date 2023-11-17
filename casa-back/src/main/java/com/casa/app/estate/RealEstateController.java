package com.casa.app.estate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/realEstate")
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @PostMapping("/create")
    public RealEstateDTO create(@RequestBody RealEstateCreateDTO estateDTO) {
        return realEstateService.create(estateDTO);
    }


    @GetMapping("/getAllByOwner")
    public List<RealEstateDTO> getAllByOwner() {
        return realEstateService.getAllByOwner();
    }
}
