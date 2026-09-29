package com.conoday.geli.warehouse.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 500) String description
) {}
