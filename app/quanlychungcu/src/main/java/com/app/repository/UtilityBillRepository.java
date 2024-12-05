package com.app.repository;

import com.app.entity.ServiceFee;
import com.app.entity.UtilityBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UtilityBillRepository extends JpaRepository<UtilityBill, Long> {

    @Query("select s from UtilityBill s where s.month = ?1 and s.year = ?2 and s.apartment.id = ?3")
    UtilityBill findByThangNamAndCanHo(Integer month, Integer year, Long canHoId);

    @Query("select s from UtilityBill s where s.apartment.id = ?1 and s.paidStatus = false and s.fee is not null ")
    List<UtilityBill> dichVuChuaDong(Long apId);
}
