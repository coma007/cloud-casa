package com.casa.app.request;

import com.casa.app.estate.RealEstateDTO;
import com.casa.app.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RealEstateRequestDTO {

    private RealEstateDTO realEstate;
    private boolean approved;
    private String comment;

    public RealEstateRequestDTO(RealEstateRequest request, User user) {
        this.realEstate = new RealEstateDTO(request.getRealEstate(), user);
        this.approved = request.isApproved();
        this.comment = request.getComment();
    }

}
