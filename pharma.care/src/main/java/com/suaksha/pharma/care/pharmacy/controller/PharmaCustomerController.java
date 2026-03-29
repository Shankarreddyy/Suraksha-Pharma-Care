package com.suaksha.pharma.care.pharmacy.controller;

import com.suaksha.pharma.care.pharmacy.entities.PharmaCustomer;
import com.suaksha.pharma.care.pharmacy.records.MedicineRequestTOInventory;
import com.suaksha.pharma.care.pharmacy.records.PurchasedSummaryResponse;
import com.suaksha.pharma.care.pharmacy.service.PharmaCustomerServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class PharmaCustomerController {
    private final PharmaCustomerServices pharmaCustomerServices;
    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody PharmaCustomer pharmaCustomer) {
        return ResponseEntity.ok().body(pharmaCustomerServices.savePharmaCustomer(pharmaCustomer).getName()+" added successfully");
    }

    @PostMapping("/purchasemedicine")
    public ResponseEntity<PurchasedSummaryResponse> purchaseMedicine(@RequestBody List<MedicineRequestTOInventory> medicinesRequestTOInventory, @RequestParam String mobileNumber) {
        return ResponseEntity.ok().body(pharmaCustomerServices.purchaseMedicine(medicinesRequestTOInventory,mobileNumber));
    }
}
