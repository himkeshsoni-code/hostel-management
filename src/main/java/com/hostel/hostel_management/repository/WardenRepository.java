package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.Warden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WardenRepository extends JpaRepository<Warden, Integer> {

    Optional<Warden> findByUsernameAndPassword(String username, String password);
}
