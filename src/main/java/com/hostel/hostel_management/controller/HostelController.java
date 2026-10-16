package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.model.Hostel;
import com.hostel.hostel_management.repository.HostelRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hostels")
public class HostelController {

    private final HostelRepository repo;

    public HostelController(HostelRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("hostels", repo.findAll());
        return "hostels/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("hostel", new Hostel());
        model.addAttribute("title", "Add Hostel");
        return "hostels/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return repo.findById(id).map(h -> {
            model.addAttribute("hostel", h);
            model.addAttribute("title", "Edit Hostel");
            return "hostels/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Hostel not found.");
            return "redirect:/hostels";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Hostel hostel, RedirectAttributes ra) {
        try {
            repo.save(hostel);
            ra.addFlashAttribute("success", "Hostel saved successfully.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Could not save: hostel name already exists or total rooms is invalid.");
        }
        return "redirect:/hostels";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            repo.deleteById(id);
            ra.addFlashAttribute("success", "Hostel deleted.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Cannot delete: this hostel still has rooms or wardens.");
        }
        return "redirect:/hostels";
    }
}
