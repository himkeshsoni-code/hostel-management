package com.hostel.hostel_management.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_log")
public class VisitorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer visitId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String visitorName;
    private String relation;
    private LocalDateTime inTime;
    private LocalDateTime outTime;

    public Integer getVisitId() { return visitId; }
    public void setVisitId(Integer visitId) { this.visitId = visitId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }
    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }
    public LocalDateTime getInTime() { return inTime; }
    public void setInTime(LocalDateTime inTime) { this.inTime = inTime; }
    public LocalDateTime getOutTime() { return outTime; }
    public void setOutTime(LocalDateTime outTime) { this.outTime = outTime; }
}
