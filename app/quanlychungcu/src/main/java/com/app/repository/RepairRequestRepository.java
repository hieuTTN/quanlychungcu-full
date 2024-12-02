package com.app.repository;

import com.app.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
}
