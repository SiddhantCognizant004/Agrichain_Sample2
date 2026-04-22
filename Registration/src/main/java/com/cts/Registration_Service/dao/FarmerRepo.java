package com.cts.Registration_Service.dao;

import com.cts.Registration_Service.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FarmerRepo extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByUsers_Id(Long userId);

    Optional<Farmer> findByEmail(String email);
}