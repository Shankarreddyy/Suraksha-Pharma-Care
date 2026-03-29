package com.suaksha.pharma.care.pharmacy.repositories;

import com.suaksha.pharma.care.pharmacy.entities.PharmaCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmaCustomersRepository extends JpaRepository<PharmaCustomer,Long> {
    PharmaCustomer findByMobilenumber(String mobileNumber);

    boolean existsByMobilenumber(String mobileNumber);
}
