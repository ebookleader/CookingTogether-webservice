package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    // JpaRepository<Entity class, pk type>
    // CRUD 메소드 자동 생성


    @Query("SELECT p FROM Products p WHERE p_name LIKE CONCAT('%',:input,'%') ORDER BY p_id DESC")
    List<Products> findAllByInput(@Param("input") String input);

    @Query("SELECT p FROM Products p WHERE p_id=:p_id")
    List<Products> findAllByPid(@Param("p_id") Long p_id);

    @Query("SELECT p FROM Products p WHERE p_city=:city ORDER BY p_id DESC")
    List<Products> findAllByCity(@Param("city") String city);

    @Query("SELECT p FROM Products p WHERE p_weekday_price>=:min and p_weekday_price<:max")
    List<Products> findAllByPrice(@Param("min") int min, @Param("max") int max);

    @Query("SELECT p FROM Products p WHERE p_weekdayPrice>=:max")
    List<Products> findAllByPriceLast(@Param("max") int max);

    @Query("SELECT p FROM Products p WHERE p_avg_rating>=:r and p_avg_rating<:rr")
    List<Products> findAllByRating(@Param("r") double r, @Param("rr") double rr);

    Page<Products> findByProductsCity(String productsCity, Pageable pageable);

    @Query("SELECT p FROM Products p WHERE p_weekday_price>=:min and p_weekday_price<:max")
    Page<Products> findByWeekdayPrice(@Param("min") int min, @Param("max") int max, Pageable pageable);

    @Query("SELECT p FROM Products p WHERE p_avg_rating>=:r and p_avg_rating<:rr")
    Page<Products> findByProductsAvgRating(@Param("r") double r, @Param("rr") double rr, Pageable pageable);

    @Query("SELECT p FROM Products p WHERE p_name LIKE CONCAT('%',:input,'%') ORDER BY p_id DESC")
    Page<Products> findByProductsName(@Param("input") String input, Pageable pageable);

    Long countByProductsCity(String p_city);

    // sidebar
    // By City
    @Query("SELECT COUNT(*) FROM Products p WHERE p_city=:p_city")
    int findEachNumByCity(@Param("p_city") String p_city);

    // By Price
    @Query("SELECT COUNT(*) FROM Products p WHERE p_weekday_price>=:min and p_weekday_price<:max")
    int findEachNumByPrice(@Param("min") int min, @Param("max") int max);

    @Query("SELECT COUNT(*) FROM Products p WHERE p_weekday_price>=:max")
    int findEachNumByPriceLast(@Param("max") int max);

    // By Rating
    @Query("SELECT COUNT(*) FROM Products p WHERE p_avg_rating>=:r and p_avg_rating<:rr")
    int findEachNumByRating(@Param("r") double r, @Param("rr") double rr);

    @Query("SELECT COUNT(*) FROM Products p WHERE p_name LIKE CONCAT('%',:input,'%')")
    int findEachNumByInput(@Param("input") String input);
}
