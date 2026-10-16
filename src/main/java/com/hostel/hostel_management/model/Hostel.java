package com.hostel.hostel_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hostel")
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hostelId;

    private String name;
    private String type;        // Boys / Girls
    private Integer totalRooms;

    public Integer getHostelId() { return hostelId; }
    public void setHostelId(Integer hostelId) { this.hostelId = hostelId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getTotalRooms() { return totalRooms; }
    public void setTotalRooms(Integer totalRooms) { this.totalRooms = totalRooms; }
}