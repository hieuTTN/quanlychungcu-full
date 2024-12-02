package com.web.repository;

import com.web.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResidentRepository extends JpaRepository<Resident, Long> {

    @Query("select u from Resident u where u.username = ?1 and u.password = ?2")
    Optional<Resident> findByUsernameAndPassword(String username, String password);
}
