package com.hostel.hostel_management.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_payment")
public class FeePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String semester;
    private BigDecimal amountDue;
    private BigDecimal amountPaid;
    private LocalDate payDate;
    private String mode;        // Cash / UPI / Card / Bank

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public BigDecimal getAmountDue() { return amountDue; }
    public void setAmountDue(BigDecimal amountDue) { this.amountDue = amountDue; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public LocalDate getPayDate() { return payDate; }
    public void setPayDate(LocalDate payDate) { this.payDate = payDate; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    // Calculated values (not database columns)
    public BigDecimal getBalance() {
        return amountDue.subtract(amountPaid);
    }

    public String getPaymentStatus() {
        if (getBalance().signum() <= 0) return "Paid";
        if (amountPaid.signum() > 0) return "Partial";
        return "Pending";
    }
}
