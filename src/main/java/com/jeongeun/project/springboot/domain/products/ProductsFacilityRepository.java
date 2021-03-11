package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductsFacilityRepository extends JpaRepository<ProductsFacility, Long> {

    @Query("SELECT pf FROM ProductsFacility pf WHERE p_id=:p_id ORDER BY pf_id")
    List<ProductsFacility> findProductsFacility(@Param("p_id") Long p_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductsFacility pf WHERE p_id=:p_id")
    void deleteProductsFacility(@Param("p_id") Long p_id);
}
