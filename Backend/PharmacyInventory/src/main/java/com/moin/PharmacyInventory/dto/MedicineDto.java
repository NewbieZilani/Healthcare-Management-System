package com.moin.PharmacyInventory.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {
        @NotBlank(message = "Medicine name is required")
        @Size(max = 255, message = "Medicine name must be at most 255 characters")
        private String medicineName;

        @NotBlank(message = "Generic is required")
        @Size(max = 255, message = "Generic must be at most 255 characters")
        private String generic;

        @NotBlank(message = "Manufacturer is required")
        @Size(max = 255, message = "Manufacturer must be at most 255 characters")
        private String manufacturer;

        @NotNull(message = "Manufacturing date is required")
        @PastOrPresent(message = "Manufacturing date must be in the past or present")
        private LocalDate manufacturingDate;

        @NotNull(message = "Expiration date is required")
        @FutureOrPresent(message = "Expiration date must be in the future or present")
        private LocalDate expirationDate;

        @NotBlank(message = "Medicine type is required")
        @Size(max = 50, message = "Medicine type must be at most 50 characters")
        private String medicineType;

        @NotNull(message = "Availability status is required")
        private Boolean isAvailable;

}
