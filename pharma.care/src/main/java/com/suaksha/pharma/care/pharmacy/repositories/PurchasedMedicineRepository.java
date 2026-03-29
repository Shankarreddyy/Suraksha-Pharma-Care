package com.suaksha.pharma.care.pharmacy.repositories;

import com.suaksha.pharma.care.pharmacy.entities.PurchasedMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedMedicineRepository extends JpaRepository <PurchasedMedicine,Long>{
}
