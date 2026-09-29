package com.conoday.geli.warehouse.config;

import com.conoday.geli.warehouse.item.Item;
import com.conoday.geli.warehouse.item.ItemRepository;
import com.conoday.geli.warehouse.variant.Variant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner seed(ItemRepository items) {
        return args -> {
            if (items.count() > 0) return;
            Item shirt = new Item("Classic T-Shirt", "Everyday cotton shirt.");
            shirt.addVariant(new Variant("Black / M", "TSH-BLK-M", new BigDecimal("149000.00"), 24));
            shirt.addVariant(new Variant("White / L", "TSH-WHT-L", new BigDecimal("149000.00"), 12));
            Item bottle = new Item("Insulated Bottle", "Reusable stainless steel bottle.");
            bottle.addVariant(new Variant("750 ml / Blue", "BOT-BLU-750", new BigDecimal("219000.00"), 8));
            items.save(shirt);
            items.save(bottle);
        };
    }
}
