package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.model.Room;
import com.hostel.hostel_management.repository.HostelRepository;
import com.hostel.hostel_management.repository.RoomRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepo;
    private final HostelRepository hostelRepo;

    public RoomController(RoomRepository roomRepo, HostelRepository hostelRepo) {
        this.roomRepo = roomRepo;
        this.hostelRepo = hostelRepo;
    }

    // List, optionally filtered by hostel
    @GetMapping
    public String list(@RequestParam(required = false) Integer hostelId, Model model) {
        List<Room> rooms = (hostelId == null)
                ? roomRepo.findAll()
                : roomRepo.findByHostelHostelId(hostelId);
        model.addAttribute("rooms", rooms);
        model.addAttribute("hostels", hostelRepo.findAll());
        model.addAttribute("selectedHostel", hostelId);
        return "rooms/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Room room = new Room();
        room.setOccupied(0);
        room.setStatus("Vacant");
        model.addAttribute("room", room);
        model.addAttribute("hostels", hostelRepo.findAll());
        model.addAttribute("title", "Add Room");
        return "rooms/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return roomRepo.findById(id).map(r -> {
            model.addAttribute("room", r);
            model.addAttribute("hostels", hostelRepo.findAll());
            model.addAttribute("title", "Edit Room");
            return "rooms/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Room not found.");
            return "redirect:/rooms";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Room room, RedirectAttributes ra) {
        if (room.getOccupied() == null) room.setOccupied(0);
        if (room.getStatus() == null || room.getStatus().isBlank()) room.setStatus("Vacant");
        try {
            roomRepo.save(room);
            ra.addFlashAttribute("success", "Room saved successfully.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error",
                    "Could not save: room number already exists in this hostel, capacity must be 1-6 and not below current occupancy.");
        }
        return "redirect:/rooms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            roomRepo.deleteById(id);
            ra.addFlashAttribute("success", "Room deleted.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Cannot delete: this room has allocation records.");
        }
        return "redirect:/rooms";
    }
}
