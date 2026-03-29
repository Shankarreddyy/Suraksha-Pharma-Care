package com.suaksha.pharma.care.pharmacy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchased_medicine_history")
public class PurchasedMedicine {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "transactionId", nullable = false)
    private MedicinePurchasedTransaction transaction;
    private String medicineName;
    private double price;
    private int quantity;
    private LocalDateTime purchaseDate;



}
