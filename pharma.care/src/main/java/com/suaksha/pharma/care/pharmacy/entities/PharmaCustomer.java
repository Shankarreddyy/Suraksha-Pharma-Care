package com.suaksha.pharma.care.pharmacy.entities;

import com.suaksha.pharma.care.pharmacy.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_customer")
public class PharmaCustomer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;
    private String name;
    @NaturalId(mutable = true)
    @Column(name = "mobile_number", nullable = false, unique = true,length = 10)
    private String mobilenumber;
    @Enumerated(value= EnumType.STRING)
    private Gender gender;
}
