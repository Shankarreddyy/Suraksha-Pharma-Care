package com.suaksha.pharma.care.pharmacy.records;

import java.util.List;

public record PurchasedSummaryResponse(List<MedicineDetailsFromInventory> medicineDetailsFromInventory, double totalPrice) {
}
