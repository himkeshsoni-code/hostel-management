package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.repository.WardenRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final WardenRepository wardenRepo;

    public LoginController(WardenRepository wardenRepo) {
        this.wardenRepo = wardenRepo;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("warden") != null) {
            return "redirect:/students";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password,
                          HttpSession session, RedirectAttributes ra) {
        return wardenRepo.findByUsernameAndPassword(username, password)
                .map(w -> {
                    session.setAttribute("warden", w);
                    return "redirect:/students";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Invalid username or password.");
                    return "redirect:/login";
                });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
