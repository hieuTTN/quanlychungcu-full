package com.web.repository;

import com.web.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {

    @Query("select r from RepairRequest r where r.apartment.id = ?1")
    List<RepairRequest> findByApartment(Long id);
}
