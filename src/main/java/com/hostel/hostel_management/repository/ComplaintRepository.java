package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findAllByOrderByComplaintIdDesc();

    List<Complaint> findByStatusOrderByComplaintIdDesc(String status);
}
