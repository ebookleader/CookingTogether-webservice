package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductsOptionRepository extends JpaRepository<ProductsOption, Long> {
    @Query("SELECT po FROM ProductsOption po WHERE p_id=:p_id ORDER BY po_id")
    List<ProductsOption> findAllProductsOption(@Param("p_id") Long p_id);

    @Query("SELECT availableCount FROM ProductsOption WHERE po_id=:po_id")
    int findOptionAvailableCountByOptionId(@Param("po_id") Long po_id);
}
