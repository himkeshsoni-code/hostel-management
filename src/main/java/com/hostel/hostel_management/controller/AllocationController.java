package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.repository.AllocationRepository;
import com.hostel.hostel_management.service.AllocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/allocations")
public class AllocationController {

    private final AllocationRepository allocRepo;
    private final AllocationService service;

    public AllocationController(AllocationRepository allocRepo, AllocationService service) {
        this.allocRepo = allocRepo;
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("allocations", allocRepo.findAllByOrderByAllocIdDesc());
        return "allocations/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("students", allocRepo.findUnallocatedStudents());
        model.addAttribute("rooms", allocRepo.findAvailableRooms());
        return "allocations/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer studentId, @RequestParam Integer roomId, RedirectAttributes ra) {
        try {
            service.allocate(studentId, roomId);
            ra.addFlashAttribute("success", "Room allocated successfully.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/allocations";
    }

    @PostMapping("/vacate/{id}")
    public String vacate(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.vacate(id);
            ra.addFlashAttribute("success", "Student vacated the room.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/allocations";
    }
}
