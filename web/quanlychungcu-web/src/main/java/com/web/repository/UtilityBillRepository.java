package com.web.repository;

import com.web.entity.UtilityBill;
import com.web.entity.VehicleFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UtilityBillRepository extends JpaRepository<UtilityBill, Long> {

    @Query("select s from UtilityBill s where s.apartment.id = ?1 and s.paidStatus = false and s.fee is not null ")
    List<UtilityBill> dichVuChuaDong(Long apId);

    @Query("select s from UtilityBill s where s.apartment.id = ?1 and s.paidStatus = true")
    List<UtilityBill> dichVuDaDong(Long apId);

    @Query("select s from UtilityBill s where s.apartment.id = ?1 and s.paidStatus = true and s.month = ?2 and s.year = ?2")
    List<UtilityBill> dichVuDaDong(Long apId, Integer month, Integer year);
}
