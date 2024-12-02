package com.web.repository;

import com.web.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
}
