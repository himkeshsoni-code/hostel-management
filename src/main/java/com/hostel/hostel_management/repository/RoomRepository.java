package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByHostelHostelId(Integer hostelId);
}
