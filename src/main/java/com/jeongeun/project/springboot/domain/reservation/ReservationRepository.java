package com.jeongeun.project.springboot.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(*) FROM Reservation WHERE ryear=:ryear and rmonth=:rmonth and rday=:rday and optionId=:optionId")
    int findReservedCountByOptionId(@Param("ryear") int ryear, @Param("rmonth") int rmonth, @Param("rday") int rday, @Param("optionId")Long optionId);

    @Query("SELECT r FROM Reservation r WHERE userId=:userId")
    List<Reservation> findByUserId(@Param("userId") Long userId);
}
