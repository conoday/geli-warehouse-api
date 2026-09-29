package com.conoday.geli.warehouse.variant;

import com.conoday.geli.warehouse.item.Item;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "variants", uniqueConstraints = @UniqueConstraint(name = "uk_variant_sku", columnNames = "sku"))
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 60)
    private String sku;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    protected Variant() {}

    public Variant(String name, String sku, BigDecimal price, int stock) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public Item getItem() { return item; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public void setItem(Item item) { this.item = item; }
    public void setName(String name) { this.name = name; }
    public void setSku(String sku) { this.sku = sku; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}
