package com.jeongeun.project.springboot.batch;

import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationRepository;
import com.jeongeun.project.springboot.domain.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Scheduler {
    @Autowired
    private ReservationRepository reservationRepository;

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    // test
//    @Scheduled(fixedDelay = 2000)
//    public void testTask1() {
//        System.out.println("Current date is: "+ LocalDateTime.now());
//    }

//    @Scheduled(cron = "0 1 0 * * *") //매일 00시 1분마다
    @Scheduled(fixedDelay = 3000)
    public void setReservationStatusToFINISHED() {
        try{
            LocalDate date = LocalDate.now();
            LocalDate yesterday = date.minusDays(1);
            int yesterdayYear = yesterday.getYear();
            int yesterdayMonth = yesterday.getMonthValue();
            int yesterdayDay = yesterday.getDayOfMonth();

//            List<Reservation> yesterdayList = reservationRepository.findYesterdayReservationList(yesterdayYear, yesterdayMonth, yesterdayDay);
            int resultNum = reservationRepository.updateYesterdayReservationStatus(ReservationStatus.FINISHED.getKey().toUpperCase(), yesterdayYear, yesterdayMonth,
                    yesterdayDay, ReservationStatus.CONFIRMED.getKey().toUpperCase());
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

}
