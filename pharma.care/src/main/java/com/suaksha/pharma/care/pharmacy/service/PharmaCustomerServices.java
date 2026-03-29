package com.suaksha.pharma.care.pharmacy.service;

import com.suaksha.pharma.care.pharmacy.entities.MedicinePurchasedTransaction;
import com.suaksha.pharma.care.pharmacy.entities.PharmaCustomer;
import com.suaksha.pharma.care.pharmacy.entities.PurchasedMedicine;
import com.suaksha.pharma.care.pharmacy.records.MedicineRequestTOInventory;
import com.suaksha.pharma.care.pharmacy.records.PurchasedSummaryResponse;
import com.suaksha.pharma.care.pharmacy.repositories.PharmaCustomersRepository;
import com.suaksha.pharma.care.pharmacy.repositories.PurchasedMedicineRepository;
import com.suaksha.pharma.care.pharmacy.repositories.PurchasedTransactionsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@RefreshScope
@Slf4j
public class PharmaCustomerServices {

    @Value("${stock.inventory.service.url}")
    private String stockInventoryServiceUrl;
    @Autowired
    private PharmaCustomersRepository pharmaCustomersRepository;
    private final RestClient restClient;
    private final PurchasedTransactionsRepository purchasedTransactionsRepository;
    private final PurchasedMedicineRepository purchasedMedicineRepository;

    public PharmaCustomer savePharmaCustomer(PharmaCustomer pharmaCustomer){
        if (pharmaCustomersRepository.existsByMobilenumber(pharmaCustomer.getMobilenumber())){
            return pharmaCustomer;
        }
        return pharmaCustomersRepository.save(pharmaCustomer);
    }
    @CircuitBreaker(name="purchaseMedicineCircuitBreaker",fallbackMethod = "purchaseMedicineFallback")
    @Retry(name = "inventory-service-retry")
    public PurchasedSummaryResponse purchaseMedicine(List<MedicineRequestTOInventory> medicineRequestTOInventoryList, String mobileNumber){
        log.info("in purchase medicine----");
        PharmaCustomer pharmaCustomer = pharmaCustomersRepository.findByMobilenumber(mobileNumber);
        if (pharmaCustomer == null){
            pharmaCustomer= new PharmaCustomer();
            pharmaCustomer.setMobilenumber(mobileNumber);
            pharmaCustomersRepository.save(pharmaCustomer);
        }
        MedicinePurchasedTransaction medicinePurchasedTransaction
                = new MedicinePurchasedTransaction();
        medicinePurchasedTransaction.setCustomer(pharmaCustomer);
        //calling stock inventory service to proceed for transaction if the medicines are available in the required quantity
        //by using rest client
        ResponseEntity<PurchasedSummaryResponse> response=restClient.post()
                .uri(stockInventoryServiceUrl+"/purchase-medicine")
                .body(medicineRequestTOInventoryList).retrieve()
                        .toEntity(PurchasedSummaryResponse.class);

        medicinePurchasedTransaction.setTotalAmount(response.getBody().totalPrice());
        medicinePurchasedTransaction=purchasedTransactionsRepository.save(medicinePurchasedTransaction);
        PurchasedSummaryResponse purchasedSummaryResponse= response.getBody();
        List<PurchasedMedicine> purchasedMedicines = purchasedSummaryResponse.medicineDetailsFromInventory().stream().map(medicineDetailsFromInventory -> {
            PurchasedMedicine purchasedMedicine = new PurchasedMedicine();
            purchasedMedicine.setMedicineName(medicineDetailsFromInventory.name());
            purchasedMedicine.setPrice(medicineDetailsFromInventory.price());
            purchasedMedicine.setQuantity(medicineDetailsFromInventory.quantity());
            purchasedMedicine.setPurchaseDate(LocalDateTime.now());
            return purchasedMedicine;
        }).toList();

        for (PurchasedMedicine purchasedMedicine : purchasedMedicines) {
            purchasedMedicine.setTransaction(medicinePurchasedTransaction);
        }
        purchasedMedicineRepository.saveAll(purchasedMedicines);
// need to create error or exception and accordingly design the response
        return purchasedSummaryResponse;
    }



    public PurchasedSummaryResponse purchaseMedicineFallback(List<MedicineRequestTOInventory> medicineRequestTOInventoryList, String mobileNumber, Throwable throwable){
        log.error("in fallback method for purchase medicine----");
        System.out.println("Exception occurred while calling inventory service: " + throwable.getMessage());
        throw new RuntimeException("Unable to process the purchase at the moment. Please try again later.");
    }

}
