package com.hostel.hostel_management.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "allocation")
public class Allocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer allocId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDate allocDate;
    private LocalDate vacateDate;
    private String status;      // Active / Vacated

    public Integer getAllocId() { return allocId; }
    public void setAllocId(Integer allocId) { this.allocId = allocId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    public LocalDate getAllocDate() { return allocDate; }
    public void setAllocDate(LocalDate allocDate) { this.allocDate = allocDate; }
    public LocalDate getVacateDate() { return vacateDate; }
    public void setVacateDate(LocalDate vacateDate) { this.vacateDate = vacateDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
