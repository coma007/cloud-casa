package com.casa.app.estate;

import com.casa.app.location.Address;
import com.casa.app.location.CityDTO;
import com.casa.app.request.RealEstateRequestDTO;
import com.casa.app.user.regular_user.dtos.RegularUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDTO {

    private Long id;
    private String name;
    private Address address;
    private String type;
    private double size;
    private int numberOfFloors;
    private CityDTO city;
    private RealEstateRequestDTO request;
    private RegularUserDTO owner; // TODO change to dto


    public RealEstateDTO(RealEstate estate) {
        this.id = estate.getId();
        this.name = estate.getName();
        this.address = estate.getAddress();
        this.type = estate.getType().name();
        this.size = estate.getSize();
        this.numberOfFloors = estate.getNumberOfFloors();
        this.city = new CityDTO(estate.getCity());
        this.request = new RealEstateRequestDTO(estate.getRequest());
        this.owner = RegularUserDTO.toDto(estate.getOwner().getUser());
    }
}
