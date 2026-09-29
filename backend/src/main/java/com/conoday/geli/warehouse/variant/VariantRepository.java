package com.conoday.geli.warehouse.variant;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VariantRepository extends JpaRepository<Variant, Long> {
    List<Variant> findByItemId(Long itemId);
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Variant v where v.id = :id")
    Optional<Variant> findByIdForUpdate(@Param("id") Long id);
}
