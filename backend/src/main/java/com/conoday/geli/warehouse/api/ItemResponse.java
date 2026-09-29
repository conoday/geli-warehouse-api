package com.conoday.geli.warehouse.api;

import com.conoday.geli.warehouse.item.Item;
import java.util.List;

public record ItemResponse(Long id, String name, String description, List<VariantResponse> variants) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getDescription(),
                item.getVariants().stream().map(VariantResponse::from).toList());
    }
}
