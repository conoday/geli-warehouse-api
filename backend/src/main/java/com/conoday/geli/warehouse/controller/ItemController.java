package com.conoday.geli.warehouse.controller;

import com.conoday.geli.warehouse.api.*;
import com.conoday.geli.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final WarehouseService service;
    public ItemController(WarehouseService service) { this.service = service; }

    @GetMapping public List<ItemResponse> list() { return service.listItems(); }
    @GetMapping("/{id}") public ItemResponse get(@PathVariable Long id) { return service.getItem(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@Valid @RequestBody ItemRequest request) { return service.createItem(request); }
    @PutMapping("/{id}") public ItemResponse update(@PathVariable Long id, @Valid @RequestBody ItemRequest request) { return service.updateItem(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.deleteItem(id); }

    @GetMapping("/{itemId}/variants") public List<VariantResponse> variants(@PathVariable Long itemId) { return service.listVariants(itemId); }
    @PostMapping("/{itemId}/variants") @ResponseStatus(HttpStatus.CREATED)
    public VariantResponse createVariant(@PathVariable Long itemId, @Valid @RequestBody VariantRequest request) { return service.createVariant(itemId, request); }
    @PutMapping("/{itemId}/variants/{variantId}")
    public VariantResponse updateVariant(@PathVariable Long itemId, @PathVariable Long variantId, @Valid @RequestBody VariantRequest request) {
        return service.updateVariant(variantId, request);
    }
    @DeleteMapping("/{itemId}/variants/{variantId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVariant(@PathVariable Long itemId, @PathVariable Long variantId) { service.deleteVariant(variantId); }
}
