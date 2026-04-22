package com.cts.Registration_Service.dao;

import com.cts.Registration_Service.entity.Users;
import com.cts.Registration_Service.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByPhone(String phone);
    Optional<Users> findByRole(UserRole role);
}