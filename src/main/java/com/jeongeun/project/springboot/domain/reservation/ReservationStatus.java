package com.jeongeun.project.springboot.domain.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    APPLIED("APPLIED", "예약신청"),
    CONFIRMED("CONFIRMED", "예약확정"),
    FINISHED("FINISHED", "완료");

    private final String key;
    private final String title;
}
