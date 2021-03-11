package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilesRepository extends JpaRepository<Files, Long> {
    @Query("SELECT f FROM Files f WHERE p_id=:p_id and isThumbnail=false")
    List<Files> findProductsFiles(@Param("p_id") Long p_id);

    @Query("SELECT f FROM Files f WHERE p_id=:p_id and isThumbnail=true")
    Files findProductsThumbnail(@Param("p_id") Long p_id);

}
