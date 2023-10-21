package com.devtechnexus.paymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.ZoneId;

/**
 * Entity class for payment service
 *
 * <h3>Fields</h3>
 * <li>int id</li>
 * <li>int userId</li>
 * <li>float amount</li>
 * <li>Timestamp datetime</li>
 * <li>String status</li>
 *
 */
@Data
@Entity
@Table(name="payment_record")
public class PaymentRecord {

    public PaymentRecord() {
    }

    public PaymentRecord(int uid, int oid, double amount, Timestamp datetime, String status, String payment_id, String currency, String description) {
        this.userId = uid;
        this.orderId = oid;
        this.amount = amount;
        this.datetime = datetime;
        this.status = status;
        this.payment_id = payment_id;
        this.currency = currency;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id")
    private int userId;

    @Column(name="order_id")
    private int orderId;

    @Column(name="amount")
    private double amount;

    @Column(name="datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp datetime;

    @Column(name="status")
    private String status;

    @Column(name="payment_id")
    private String payment_id;

    @Column(name="currency")
    private String currency;

    @Column(name="description")
    private String description;


    /**
     * @return Datetime as UTC
     */
    public String parseTimestamp() {
        return datetime.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime() + " UTC";

    }
}
