package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RealEstateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private RealEstate realEstate;

    private boolean approved;

    private String comment;

    public RealEstateRequest(RealEstate estate) {
        this.realEstate = estate;
        this.approved = false;
        this.comment = "";
    }
}
