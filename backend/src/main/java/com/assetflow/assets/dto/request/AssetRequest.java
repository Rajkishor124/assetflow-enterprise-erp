package com.assetflow.assets.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AssetRequest {

    @NotBlank(message = "Asset tag is required")
    @Size(max = 50, message = "Asset tag must not exceed 50 characters")
    private String assetTag;

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    private String description;

    @NotBlank(message = "Serial number is required")
    @Size(max = 100, message = "Serial number must not exceed 100 characters")
    private String serialNumber;

    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;

    @Size(max = 100, message = "Model must not exceed 100 characters")
    private String model;

    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private LocalDate warrantyExpiry;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
