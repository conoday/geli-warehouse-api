package com.conoday.geli.warehouse.controller;

import com.conoday.geli.warehouse.api.*;
import com.conoday.geli.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variants")
public class VariantController {
    private final WarehouseService service;
    public VariantController(WarehouseService service) { this.service = service; }
    @GetMapping("/{id}") public VariantResponse get(@PathVariable Long id) { return service.getVariant(id); }
    @PostMapping("/{id}/stock-adjustments")
    public VariantResponse adjust(@PathVariable Long id, @Valid @RequestBody QuantityRequest request) { return service.adjustStock(id, request); }
    @PostMapping("/{id}/sales")
    public VariantResponse sell(@PathVariable Long id, @Valid @RequestBody QuantityRequest request) { return service.sell(id, request); }
}
