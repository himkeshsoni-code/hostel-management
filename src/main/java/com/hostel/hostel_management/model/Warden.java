package com.hostel.hostel_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "warden")
public class Warden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wardenId;

    @ManyToOne
    @JoinColumn(name = "hostel_id", nullable = false)
    private Hostel hostel;

    private String name;
    private String phone;
    private String username;
    private String password;

    public Integer getWardenId() { return wardenId; }
    public void setWardenId(Integer wardenId) { this.wardenId = wardenId; }
    public Hostel getHostel() { return hostel; }
    public void setHostel(Hostel hostel) { this.hostel = hostel; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
