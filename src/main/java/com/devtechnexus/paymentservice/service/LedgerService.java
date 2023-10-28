package com.devtechnexus.paymentservice.service;

import com.devtechnexus.paymentservice.dto.PaymentDto;
import com.devtechnexus.paymentservice.model.PaymentRecord;
import com.devtechnexus.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class LedgerService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * run when payment is CREATED
     */
    public void createLedgerEntry(String id, PaymentDto payment) {


        paymentRepository.save(new PaymentRecord(
                payment.getUser(),
                payment.getOid(),
                payment.getPrice(),
                Timestamp.valueOf(LocalDateTime.now()),
                "PENDING",
                id,
                payment.getCurrency(),
                payment.getDescription()));
    }

    /**
     * run when payment is COMPLETED or CANCELLED
     */
    public void successLedgerEntry(String uid, int oid, String paymentid) {
        PaymentRecord paymentRecord = paymentRepository.findByUserAndOrderId(uid, oid);
        paymentRecord.setStatus("COMPLETED");
        paymentRecord.setPayment_id(paymentid);
        paymentRepository.save(paymentRecord);

    }

    public void cancelLedgerEntry(String uid, int oid) {
        PaymentRecord paymentRecord = paymentRepository.findByUserAndOrderId(uid, oid);
        paymentRecord.setStatus("CANCELLED");
        paymentRepository.save(paymentRecord);
    }

}
