package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.Allocation;
import com.hostel.hostel_management.model.Room;
import com.hostel.hostel_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Integer> {

    List<Allocation> findAllByOrderByAllocIdDesc();

    boolean existsByStudentStudentIdAndStatus(Integer studentId, String status);

    // Students who currently have no active room
    @Query("select s from Student s where s.studentId not in "
            + "(select a.student.studentId from Allocation a where a.status = 'Active') "
            + "order by s.name")
    List<Student> findUnallocatedStudents();

    // Rooms that still have a free bed
    @Query("select r from Room r where r.occupied < r.capacity order by r.hostel.name, r.roomNo")
    List<Room> findAvailableRooms();
}
