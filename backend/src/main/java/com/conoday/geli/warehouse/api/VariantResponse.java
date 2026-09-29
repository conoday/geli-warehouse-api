package com.conoday.geli.warehouse.api;

import com.conoday.geli.warehouse.variant.Variant;
import java.math.BigDecimal;

public record VariantResponse(Long id, Long itemId, String itemName, String name, String sku,
                              BigDecimal price, int stock) {
    public static VariantResponse from(Variant variant) {
        return new VariantResponse(variant.getId(), variant.getItem().getId(), variant.getItem().getName(),
                variant.getName(), variant.getSku(), variant.getPrice(), variant.getStock());
    }
}
