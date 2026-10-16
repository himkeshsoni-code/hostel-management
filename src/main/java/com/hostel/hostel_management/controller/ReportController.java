package com.hostel.hostel_management.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @PersistenceContext
    private EntityManager em;

    @GetMapping
    public String reports(Model model) {
        // Both reads come from database VIEWS defined in day6_database_features.sql
        model.addAttribute("defaulters", em.createNativeQuery(
                "SELECT roll_no, name, phone, total_due, total_paid, balance FROM fee_defaulters").getResultList());
        model.addAttribute("vacant", em.createNativeQuery(
                "SELECT hostel, hostel_type, room_no, floor, capacity, occupied, free_beds FROM vacant_rooms").getResultList());
        return "reports/index";
    }
}
