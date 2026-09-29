package com.conoday.geli.warehouse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI warehouseOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("GELI Warehouse API")
                .version("1.0.0")
                .description("Inventory and sales API for the GELI warehouse assessment."));
    }
}
