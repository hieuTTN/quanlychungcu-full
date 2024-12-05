package com.app.repository;

import com.app.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    @Query("select a from Apartment a where a.isSold = true")
    List<Apartment> canHoDaBan();
}
