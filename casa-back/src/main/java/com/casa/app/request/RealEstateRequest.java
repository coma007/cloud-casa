package com.casa.app.request;

import com.casa.app.estate.RealEstate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RealEstateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private RealEstate realEstate;

    private boolean approved;
    private boolean declined;

    private String comment;

    public RealEstateRequest(RealEstate estate) {
        this.realEstate = estate;
        this.approved = false;
        this.declined = false;
        this.comment = "";
    }
}
