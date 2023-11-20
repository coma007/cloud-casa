    package com.casa.app.location;

    import com.casa.app.estate.RealEstate;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface CityRepository extends JpaRepository<City, Long> {
        City getCityByName(String name);

        List<City> getCityByCountryName(String name);
    }
