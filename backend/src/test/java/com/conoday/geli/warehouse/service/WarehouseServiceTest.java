package com.conoday.geli.warehouse.service;

import com.conoday.geli.warehouse.api.QuantityRequest;
import com.conoday.geli.warehouse.exception.InsufficientStockException;
import com.conoday.geli.warehouse.item.Item;
import com.conoday.geli.warehouse.item.ItemRepository;
import com.conoday.geli.warehouse.variant.Variant;
import com.conoday.geli.warehouse.variant.VariantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {
    @Mock ItemRepository itemRepository;
    @Mock VariantRepository variantRepository;

    @Test
    void saleReducesStock() {
        Variant variant = new Variant("M", "SKU-1", BigDecimal.TEN, 5);
        Item item = new Item("Shirt", "desc");
        item.addVariant(variant);
        when(variantRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(variant));
        WarehouseService service = new WarehouseService(itemRepository, variantRepository);

        service.sell(1L, new QuantityRequest(2, "order"));

        assertEquals(3, variant.getStock());
    }

    @Test
    void saleRejectsInsufficientStock() {
        Variant variant = new Variant("M", "SKU-1", BigDecimal.TEN, 1);
        when(variantRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(variant));
        WarehouseService service = new WarehouseService(itemRepository, variantRepository);

        assertThrows(InsufficientStockException.class, () -> service.sell(1L, new QuantityRequest(2, null)));
        assertEquals(1, variant.getStock());
    }
}
