package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCase(String name, String rollNo);
}
