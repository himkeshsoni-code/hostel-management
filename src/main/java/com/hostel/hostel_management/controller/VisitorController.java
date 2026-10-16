package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.model.Student;
import com.hostel.hostel_management.model.VisitorLog;
import com.hostel.hostel_management.repository.StudentRepository;
import com.hostel.hostel_management.repository.VisitorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/visitors")
public class VisitorController {

    private final VisitorRepository visitorRepo;
    private final StudentRepository studentRepo;

    public VisitorController(VisitorRepository visitorRepo, StudentRepository studentRepo) {
        this.visitorRepo = visitorRepo;
        this.studentRepo = studentRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("visits", visitorRepo.findAllByOrderByVisitIdDesc());
        return "visitors/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("students", studentRepo.findAll());
        return "visitors/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer studentId, @RequestParam String visitorName,
                       @RequestParam String relation, RedirectAttributes ra) {
        Student student = studentRepo.findById(studentId).orElse(null);
        if (student == null) {
            ra.addFlashAttribute("error", "Student not found.");
            return "redirect:/visitors";
        }
        VisitorLog v = new VisitorLog();
        v.setStudent(student);
        v.setVisitorName(visitorName);
        v.setRelation(relation);
        v.setInTime(LocalDateTime.now());
        visitorRepo.save(v);
        ra.addFlashAttribute("success", "Visitor checked in.");
        return "redirect:/visitors";
    }

    @PostMapping("/checkout/{id}")
    public String checkout(@PathVariable Integer id, RedirectAttributes ra) {
        VisitorLog v = visitorRepo.findById(id).orElse(null);
        if (v == null || v.getOutTime() != null) {
            ra.addFlashAttribute("error", "Visit not found or already checked out.");
            return "redirect:/visitors";
        }
        v.setOutTime(LocalDateTime.now());
        visitorRepo.save(v);
        ra.addFlashAttribute("success", "Visitor checked out.");
        return "redirect:/visitors";
    }
}
