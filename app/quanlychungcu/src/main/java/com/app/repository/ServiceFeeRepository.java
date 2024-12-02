package com.app.repository;

import com.app.entity.ServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {
}
