package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.model.Student;
import com.hostel.hostel_management.repository.StudentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    // List + search
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<Student> students = (keyword == null || keyword.isBlank())
                ? repo.findAll()
                : repo.findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCase(keyword, keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "students/list";
    }

    // Show empty form
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("title", "Add Student");
        return "students/form";
    }

    // Show form filled with existing student
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return repo.findById(id).map(s -> {
            model.addAttribute("student", s);
            model.addAttribute("title", "Edit Student");
            return "students/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Student not found.");
            return "redirect:/students";
        });
    }

    // Create or update
    @PostMapping("/save")
    public String save(@ModelAttribute Student student, RedirectAttributes ra) {
        try {
            repo.save(student);
            ra.addFlashAttribute("success", "Student saved successfully.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Could not save: roll number or email already exists.");
        }
        return "redirect:/students";
    }

    // Delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            repo.deleteById(id);
            ra.addFlashAttribute("success", "Student deleted.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error",
                    "Cannot delete: this student has a room allocation, payments or complaints.");
        }
        return "redirect:/students";
    }
}
