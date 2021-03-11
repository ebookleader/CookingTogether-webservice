package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductsPolicyRepository extends JpaRepository<ProductsPolicy, Long> {
    @Query("SELECT pp from ProductsPolicy pp WHERE p_id=:p_id ORDER BY pp_id")
    List<ProductsPolicy> findProductsPolicy(@Param("p_id") Long p_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductsPolicy pp WHERE p_id=:p_id")
    void deleteProductsPolicy(@Param("p_id") Long p_id);
}
