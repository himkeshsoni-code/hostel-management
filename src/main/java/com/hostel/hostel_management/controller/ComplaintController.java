package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.model.Complaint;
import com.hostel.hostel_management.model.Student;
import com.hostel.hostel_management.repository.ComplaintRepository;
import com.hostel.hostel_management.repository.StudentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintRepository complaintRepo;
    private final StudentRepository studentRepo;

    public ComplaintController(ComplaintRepository complaintRepo, StudentRepository studentRepo) {
        this.complaintRepo = complaintRepo;
        this.studentRepo = studentRepo;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        List<Complaint> complaints = (status == null || status.isBlank())
                ? complaintRepo.findAllByOrderByComplaintIdDesc()
                : complaintRepo.findByStatusOrderByComplaintIdDesc(status);
        model.addAttribute("complaints", complaints);
        model.addAttribute("selected", status);
        return "complaints/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("students", studentRepo.findAll());
        return "complaints/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer studentId, @RequestParam String category,
                       @RequestParam String description, RedirectAttributes ra) {
        Student student = studentRepo.findById(studentId).orElse(null);
        if (student == null) {
            ra.addFlashAttribute("error", "Student not found.");
            return "redirect:/complaints";
        }
        Complaint c = new Complaint();
        c.setStudent(student);
        c.setCategory(category);
        c.setDescription(description);
        c.setStatus("Open");
        c.setRaisedOn(LocalDate.now());
        try {
            complaintRepo.save(c);
            ra.addFlashAttribute("success", "Complaint registered.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Could not save the complaint.");
        }
        return "redirect:/complaints";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Integer id, @RequestParam String status, RedirectAttributes ra) {
        Complaint c = complaintRepo.findById(id).orElse(null);
        if (c == null) {
            ra.addFlashAttribute("error", "Complaint not found.");
            return "redirect:/complaints";
        }
        c.setStatus(status);
        c.setResolvedOn("Resolved".equals(status) ? LocalDate.now() : null);
        complaintRepo.save(c);
        ra.addFlashAttribute("success", "Status updated to " + status + ".");
        return "redirect:/complaints";
    }
}
