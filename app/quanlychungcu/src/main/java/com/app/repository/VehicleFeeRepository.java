package com.app.repository;

import com.app.entity.UtilityBill;
import com.app.entity.VehicleFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleFeeRepository extends JpaRepository<VehicleFee, Long> {

    @Query("select s from VehicleFee s where s.month = ?1 and s.year = ?2 and s.apartment.id = ?3")
    VehicleFee findByThangNamAndCanHo(Integer month, Integer year, Long canHoId);

    @Query("select s from VehicleFee s where s.apartment.id = ?1 and s.paidStatus = false and s.fee > 0")
    List<VehicleFee> dichVuChuaDong(Long apId);
}
