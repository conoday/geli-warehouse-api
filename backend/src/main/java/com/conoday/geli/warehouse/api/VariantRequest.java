package com.conoday.geli.warehouse.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record VariantRequest(
        @NotBlank @Size(max = 80) String name,
        @NotBlank @Size(max = 60) String sku,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price,
        @Min(0) int stock
) {}
