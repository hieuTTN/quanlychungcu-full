package com.web.repository;

import com.web.entity.ServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {

    @Query("select s from ServiceFee s where s.apartment.id = ?1 and s.paidStatus = false")
    List<ServiceFee> dichVuChuaDong(Long apId);

    @Query("select s from ServiceFee s where s.apartment.id = ?1 and s.paidStatus = true")
    List<ServiceFee> dichVuDaDong(Long apId);

    @Query("select s from ServiceFee s where s.apartment.id = ?1 and s.paidStatus = true and s.month = ?2 and s.year = ?2")
    List<ServiceFee> dichVuDaDong(Long apId, Integer month, Integer year);
}
