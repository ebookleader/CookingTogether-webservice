package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    // JpaRepository<Entity class, pk type>
    // CRUD 메소드 자동 생성

    @Query("SELECT p FROM Products p ORDER BY p_id DESC")
    List<Products> findAllDesc();

    @Query("SELECT p FROM Products p WHERE p_name LIKE CONCAT('%',:input,'%') ORDER BY p_id DESC")
    List<Products> findAllByInput(@Param("input") String input);

//    @Query("SELECT p FROM Products p WHERE p_category=:category ORDER BY p_id DESC")
//    List<Products> findAllByCategory(String category);

    @Query("SELECT p FROM Products p WHERE p_city=:city ORDER BY p_id DESC")
    List<Products> findAllByCity(@Param("city") String city);

    @Query("SELECT p FROM Products p WHERE p_weekdayPrice>=:min and p_weekdayPrice<:max")
    List<Products> findAllByPrice(@Param("min") int min, @Param("max") int max);

    @Query("SELECT p FROM Products p WHERE p_weekdayPrice>=:max")
    List<Products> findAllByPriceLast(@Param("max") int max);

    @Query("SELECT p FROM Products p WHERE p_avgRating>=:r and p_avgRating<:rr")
    List<Products> findAllByRating(@Param("r") double r, @Param("rr") double rr);

    // sidebar
    // By City
    @Query("SELECT COUNT(*) FROM Products p WHERE p_city=:p_city")
    int findEachNumByCity(@Param("p_city") String p_city);

    // By Category
//    @Query("SELECT COUNT(*) FROM Products p WHERE p_category=:p_category")
//    int findEachNumByCategory(@Param("p_category") String p_category);

    // By Price
    @Query("SELECT COUNT(*) FROM Products p WHERE p_weekdayPrice>=:min and p_weekdayPrice<:max")
    int findEachNumByPrice(@Param("min") int min, @Param("max") int max);

    @Query("SELECT COUNT(*) FROM Products p WHERE p_weekdayPrice>=:max")
    int findEachNumByPriceLast(@Param("max") int max);

    // By Rating
    @Query("SELECT COUNT(*) FROM Products p WHERE p_avgRating>=:r and p_avgRating<:rr")
    int findEachNumByRating(@Param("r") double r, @Param("rr") double rr);
}
