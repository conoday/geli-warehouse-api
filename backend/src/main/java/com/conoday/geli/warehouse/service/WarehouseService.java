package com.conoday.geli.warehouse.service;

import com.conoday.geli.warehouse.api.*;
import com.conoday.geli.warehouse.exception.*;
import com.conoday.geli.warehouse.item.*;
import com.conoday.geli.warehouse.variant.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WarehouseService {
    private final ItemRepository itemRepository;
    private final VariantRepository variantRepository;

    public WarehouseService(ItemRepository itemRepository, VariantRepository variantRepository) {
        this.itemRepository = itemRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> listItems() { return itemRepository.findAll().stream().map(ItemResponse::from).toList(); }

    @Transactional(readOnly = true)
    public ItemResponse getItem(Long id) { return ItemResponse.from(item(id)); }

    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        return ItemResponse.from(itemRepository.save(new Item(request.name().trim(), request.description())));
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = item(id);
        item.setName(request.name().trim());
        item.setDescription(request.description());
        return ItemResponse.from(item);
    }

    @Transactional
    public void deleteItem(Long id) { itemRepository.delete(item(id)); }

    @Transactional(readOnly = true)
    public List<VariantResponse> listVariants(Long itemId) {
        item(itemId);
        return variantRepository.findByItemId(itemId).stream().map(VariantResponse::from).toList();
    }

    @Transactional
    public VariantResponse createVariant(Long itemId, VariantRequest request) {
        Item item = item(itemId);
        ensureSkuAvailable(request.sku().trim(), null);
        Variant variant = new Variant(request.name().trim(), request.sku().trim(), request.price(), request.stock());
        item.addVariant(variant);
        return VariantResponse.from(variantRepository.save(variant));
    }

    @Transactional(readOnly = true)
    public VariantResponse getVariant(Long id) { return VariantResponse.from(variant(id)); }

    @Transactional
    public VariantResponse updateVariant(Long id, VariantRequest request) {
        Variant variant = variant(id);
        ensureSkuAvailable(request.sku().trim(), id);
        variant.setName(request.name().trim());
        variant.setSku(request.sku().trim());
        variant.setPrice(request.price());
        variant.setStock(request.stock());
        return VariantResponse.from(variant);
    }

    @Transactional
    public void deleteVariant(Long id) { variantRepository.delete(variant(id)); }

    @Transactional
    public VariantResponse adjustStock(Long id, QuantityRequest request) {
        Variant variant = lockedVariant(id);
        variant.setStock(variant.getStock() + request.quantity());
        return VariantResponse.from(variant);
    }

    @Transactional
    public VariantResponse sell(Long id, QuantityRequest request) {
        Variant variant = lockedVariant(id);
        if (variant.getStock() < request.quantity()) {
            throw new InsufficientStockException("Insufficient stock for SKU " + variant.getSku()
                    + ". Available: " + variant.getStock() + ", requested: " + request.quantity());
        }
        variant.setStock(variant.getStock() - request.quantity());
        return VariantResponse.from(variant);
    }

    private Item item(Long id) { return itemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item " + id + " was not found")); }
    private Variant variant(Long id) { return variantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Variant " + id + " was not found")); }
    private Variant lockedVariant(Long id) { return variantRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new ResourceNotFoundException("Variant " + id + " was not found")); }
    private void ensureSkuAvailable(String sku, Long currentId) {
        boolean used = currentId == null ? variantRepository.existsBySku(sku) : variantRepository.existsBySkuAndIdNot(sku, currentId);
        if (used) throw new DuplicateSkuException("SKU already exists: " + sku);
    }
}
