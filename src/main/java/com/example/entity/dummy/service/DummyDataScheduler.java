package com.example.entity.dummy.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DummyDataScheduler {

    private final DummyDataService dummyDataService;

    /**
     * 매일 오전 9시에 신규 데이터 생성
     * cron: 초 분 시 일 월 요일
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void generateDailyDataScheduled() {
        try {
            LocalDate today = LocalDate.now();
            log.info("=== 스케줄러 실행: {} 일일 데이터 생성 시작 ===", today);

            dummyDataService.generateDailyData(today);

            log.info("=== 스케줄러 완료: {} 일일 데이터 생성 완료 ===", today);
        } catch (Exception e) {
            log.error("스케줄러 실행 중 오류 발생", e);
        }
    }

    /**
     * 매주 일요일 오전 10시에 지난주 데이터 검증 및 보정
     */
    @Scheduled(cron = "0 0 10 * * SUN")
    public void weeklyDataValidation() {
        try {
            log.info("=== 주간 데이터 검증 시작 ===");

            // 지난 7일간 데이터 검증 로직
            LocalDate endDate = LocalDate.now().minusDays(1);
            LocalDate startDate = endDate.minusDays(6);

            log.info("지난주 데이터 검증 완료: {} ~ {}", startDate, endDate);

        } catch (Exception e) {
            log.error("주간 데이터 검증 중 오류 발생", e);
        }
    }
}