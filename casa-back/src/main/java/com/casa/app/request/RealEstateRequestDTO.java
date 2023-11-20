package com.casa.app.request;

import com.casa.app.estate.RealEstateCreateDTO;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RealEstateRequestDTO {

    private Long id;
    private boolean approved;
    private boolean declined;
    private String comment;

    public RealEstateRequestDTO(RealEstateRequest request) {
        this.id = request.getId();
        this.approved = request.isApproved();
        this.declined = request.isDeclined();
        this.comment = request.getComment();
    }

}
