package com.jeongeun.project.springboot.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(*) FROM Reservation WHERE ryear=:ryear and rmonth=:rmonth and rday=:rday and optionId=:optionId")
    int findReservedCountByOptionId(@Param("ryear") int ryear, @Param("rmonth") int rmonth, @Param("rday") int rday, @Param("optionId")Long optionId);

    @Query("SELECT r FROM Reservation r WHERE userId=:userId and reservation_status!=:finished ")
    List<Reservation> findByUserId(@Param("userId") Long userId, @Param("finished") String finished);

    Reservation findByRid(Long rid);

    @Query("SELECT r FROM Reservation r WHERE userId=:userId and reservation_status=:finished ")
    List<Reservation> findPreviousByUserId(@Param("userId") Long userId, @Param("finished") String finished);

    @Query("SELECT r FROM Reservation r WHERE ryear=:ryear and rmonth=:rmonth and rday=:rday")
    List<Reservation> findYesterdayReservationList(@Param("ryear") int ryear, @Param("rmonth") int rmonth, @Param("rday") int rday);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation SET reservation_status=:finished where ryear=:ryear and rmonth=:rmonth and rday=:rday and reservation_status=:confirmed")
    int updateYesterdayReservationStatus(@Param("finished") String finished, @Param("ryear") int ryear, @Param("rmonth") int rmonth, @Param("rday") int rday, @Param("confirmed") String confirmed);

    // SELECT * FROM Reservation WHERE ryear=:year and rmonth=:month and rday=:day
}
