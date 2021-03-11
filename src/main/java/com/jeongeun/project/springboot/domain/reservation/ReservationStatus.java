package com.jeongeun.project.springboot.domain.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    APPLIED("Applied", "예약신청"),
    CONFIRMED("Confirmed", "예약확정"),
    FINISHED("Finished", "완료");

    private final String key;
    private final String title;
}
