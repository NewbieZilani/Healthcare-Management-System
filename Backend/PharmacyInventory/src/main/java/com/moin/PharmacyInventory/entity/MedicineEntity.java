package com.moin.PharmacyInventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Medicine")
public class MedicineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String medicineName;

    @Column(nullable = false)
    private String generic;

    @Column(nullable = false)
    private String manufacturer;

    @Column(name = "manufacturingDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate manufacturingDate;

    @Column(name = "expirationDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private String medicineType;

    @Column(nullable = false)
    private Boolean isAvailable;

}