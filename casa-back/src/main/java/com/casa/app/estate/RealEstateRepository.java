package com.casa.app.estate;

import com.casa.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {

    List<RealEstate> getAllByOwnerUser(User user);
}