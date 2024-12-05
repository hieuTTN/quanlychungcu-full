package com.app.repository;

import com.app.entity.ServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {

    @Query("select s from ServiceFee s where s.month = ?1 and s.year = ?2 and s.apartment.id = ?3")
    ServiceFee findByThangNamAndCanHo(Integer month, Integer year, Long canHoId);
}
