package com.suaksha.pharma.care.pharmacy.repositories;

import com.suaksha.pharma.care.pharmacy.entities.MedicinePurchasedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedTransactionsRepository extends JpaRepository<MedicinePurchasedTransaction,Long> {
}
