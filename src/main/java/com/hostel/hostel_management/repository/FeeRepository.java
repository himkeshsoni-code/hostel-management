package com.hostel.hostel_management.repository;

import com.hostel.hostel_management.model.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeeRepository extends JpaRepository<FeePayment, Integer> {

    List<FeePayment> findAllByOrderByPaymentIdDesc();

    // Records with an unpaid balance
    @Query("select f from FeePayment f where f.amountPaid < f.amountDue order by f.paymentId desc")
    List<FeePayment> findPending();
}
