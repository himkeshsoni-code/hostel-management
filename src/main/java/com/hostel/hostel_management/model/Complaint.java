package com.hostel.hostel_management.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer complaintId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String category;    // Electrical / Plumbing / Cleaning / Food / Other
    private String description;
    private String status;      // Open / In Progress / Resolved
    private LocalDate raisedOn;
    private LocalDate resolvedOn;

    public Integer getComplaintId() { return complaintId; }
    public void setComplaintId(Integer complaintId) { this.complaintId = complaintId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getRaisedOn() { return raisedOn; }
    public void setRaisedOn(LocalDate raisedOn) { this.raisedOn = raisedOn; }
    public LocalDate getResolvedOn() { return resolvedOn; }
    public void setResolvedOn(LocalDate resolvedOn) { this.resolvedOn = resolvedOn; }
}
