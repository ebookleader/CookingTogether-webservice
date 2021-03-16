package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductsReviewRepository extends JpaRepository<ProductsReview, Long> {

    @Query("SELECT COUNT(*) FROM ProductsReview pr WHERE p_id=:p_id AND userId=:userId")
    Long findUserWroteReview(@Param("p_id") Long p_id, @Param("userId") Long userId);

    @Query("SELECT COUNT(*) FROM ProductsReview pr WHERE reservationId=:reservationId")
    Long countByReservationId(@Param("reservationId") Long reservationId);

    @Query("SELECT pr FROM ProductsReview pr WHERE p_id=:p_id ORDER BY reviewId DESC")
    List<ProductsReview> findProductsReviewByPid(@Param("p_id") Long p_id);
}
