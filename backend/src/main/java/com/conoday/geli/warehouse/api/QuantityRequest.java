package com.conoday.geli.warehouse.api;

import jakarta.validation.constraints.Min;

public record QuantityRequest(@Min(1) int quantity, String reason) {}
