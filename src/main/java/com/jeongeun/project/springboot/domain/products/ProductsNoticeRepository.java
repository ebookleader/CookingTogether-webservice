package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductsNoticeRepository extends JpaRepository<ProductsNotice, Long> {
    @Query("SELECT pn FROM ProductsNotice pn WHERE p_id=:p_id ORDER BY pn_id")
    List<ProductsNotice> findProductsNotice(@Param("p_id") Long p_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductsNotice pn WHERE p_id=:p_id")
    void deleteProductsNotice(@Param("p_id") Long p_id);

}
