package com.conoday.geli.warehouse.item;

import com.conoday.geli.warehouse.variant.Variant;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Variant> variants = new ArrayList<>();

    protected Item() {}

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Variant> getVariants() { return variants; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void addVariant(Variant variant) { variants.add(variant); variant.setItem(this); }
}
