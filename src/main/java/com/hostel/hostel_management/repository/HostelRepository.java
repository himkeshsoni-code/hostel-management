package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelRepository extends JpaRepository<Hostel, Integer> {
}
