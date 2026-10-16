package com.hostel.hostel_management.controller;

import com.hostel.hostel_management.model.FeePayment;
import com.hostel.hostel_management.model.Student;
import com.hostel.hostel_management.repository.FeeRepository;
import com.hostel.hostel_management.repository.StudentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/fees")
public class FeeController {

    private final FeeRepository feeRepo;
    private final StudentRepository studentRepo;

    public FeeController(FeeRepository feeRepo, StudentRepository studentRepo) {
        this.feeRepo = feeRepo;
        this.studentRepo = studentRepo;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "false") boolean pending, Model model) {
        List<FeePayment> fees = pending ? feeRepo.findPending() : feeRepo.findAllByOrderByPaymentIdDesc();
        BigDecimal outstanding = fees.stream()
                .map(FeePayment::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("fees", fees);
        model.addAttribute("pending", pending);
        model.addAttribute("outstanding", outstanding);
        return "fees/list";
    }

    // Create a new fee record (amount due for a semester)
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("students", studentRepo.findAll());
        return "fees/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer studentId, @RequestParam String semester,
                       @RequestParam BigDecimal amountDue, RedirectAttributes ra) {
        Student student = studentRepo.findById(studentId).orElse(null);
        if (student == null) {
            ra.addFlashAttribute("error", "Student not found.");
            return "redirect:/fees";
        }
        if (amountDue.signum() <= 0) {
            ra.addFlashAttribute("error", "Amount due must be greater than zero.");
            return "redirect:/fees";
        }
        FeePayment f = new FeePayment();
        f.setStudent(student);
        f.setSemester(semester);
        f.setAmountDue(amountDue);
        f.setAmountPaid(BigDecimal.ZERO);
        try {
            feeRepo.save(f);
            ra.addFlashAttribute("success", "Fee record created.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Could not save the fee record.");
        }
        return "redirect:/fees";
    }

    // Record a payment against a fee record
    @GetMapping("/pay/{id}")
    public String payForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return feeRepo.findById(id).map(f -> {
            model.addAttribute("fee", f);
            return "fees/pay";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Fee record not found.");
            return "redirect:/fees";
        });
    }

    @PostMapping("/pay/{id}")
    public String pay(@PathVariable Integer id, @RequestParam BigDecimal amount,
                      @RequestParam String mode, RedirectAttributes ra) {
        FeePayment f = feeRepo.findById(id).orElse(null);
        if (f == null) {
            ra.addFlashAttribute("error", "Fee record not found.");
            return "redirect:/fees";
        }
        if (amount.signum() <= 0 || amount.compareTo(f.getBalance()) > 0) {
            ra.addFlashAttribute("error", "Enter an amount between 1 and the pending balance (" + f.getBalance() + ").");
            return "redirect:/fees";
        }
        f.setAmountPaid(f.getAmountPaid().add(amount));
        f.setPayDate(LocalDate.now());
        f.setMode(mode);
        feeRepo.save(f);
        ra.addFlashAttribute("success", "Payment recorded.");
        return "redirect:/fees";
    }
}
