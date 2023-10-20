package com.devtechnexus.paymentservice.repository;

import com.devtechnexus.paymentservice.model.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentRecord, Integer> {
}
