package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorRepository extends JpaRepository<VisitorLog, Integer> {

    List<VisitorLog> findAllByOrderByVisitIdDesc();
}
