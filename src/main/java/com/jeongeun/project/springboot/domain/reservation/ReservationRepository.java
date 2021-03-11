package com.jeongeun.project.springboot.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(*) FROM Reservation WHERE ryear=:ryear and rmonth=:rmonth and rday=:rday and optionId=:optionId")
    int findReservedCountByOptionId(@Param("ryear") int ryear, @Param("rmonth") int rmonth, @Param("rday") int rday, @Param("optionId")Long optionId);

}
