package com.conoday.geli.warehouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/api/health")
    public Map<String, Object> health() { return Map.of("status", "ok", "service", "geli-warehouse-api", "timestamp", Instant.now()); }
}
