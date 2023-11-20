package com.casa.app.estate;

import com.casa.app.location.Address;
import com.casa.app.request.RealEstateRequest;
import com.casa.app.location.City;
import com.casa.app.request.RealEstateRequestDTO;
import com.casa.app.user.User;
import com.casa.app.user.regular_user.dtos.RegularUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDTO {

    private String name;
    private Address address;
    private String type;
    private double size;
    private int numberOfFloors;
    private City city;
    private RealEstateRequestDTO request;
    private RegularUserDTO owner; // TODO change to dto


    public RealEstateDTO(RealEstate estate) {
        this.name = estate.getName();
        this.address = estate.getAddress();
        this.type = estate.getType().name();
        this.size = estate.getSize();
        this.numberOfFloors = estate.getNumberOfFloors();
        this.city = estate.getCity();
        this.request = new RealEstateRequestDTO(estate.getRequest());
        this.owner = RegularUserDTO.toDto(estate.getOwner().getUser());
    }
}
