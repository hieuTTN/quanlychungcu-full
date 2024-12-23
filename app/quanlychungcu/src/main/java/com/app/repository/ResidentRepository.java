package com.app.repository;

import com.app.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {

    @Query("select r from Resident r where r.username = ?1 and r.username is not null and r.username <> ''")
    Resident findByUserName(String username);

    @Query("select r from Resident r where r.username = ?1 and r.id <> ?2 and r.username is not null and r.username <> ''")
    Resident findByUserNameAndId(String username, Long id);

    @Query("select r from Resident r where r.apartment.floor = ?1")
    List<Resident> findByTang(Integer tang);
}
