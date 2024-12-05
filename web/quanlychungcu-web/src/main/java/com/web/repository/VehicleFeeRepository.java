package com.web.repository;

import com.web.entity.ServiceFee;
import com.web.entity.VehicleFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleFeeRepository extends JpaRepository<VehicleFee, Long> {

    @Query("select s from VehicleFee s where s.apartment.id = ?1 and s.paidStatus = false and s.fee > 0")
    List<VehicleFee> dichVuChuaDong(Long apId);
}
