package com.example.entity.dummy.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.dummy.service.DummyDataService;
import com.example.entity.dummy.service.UserTypeClassificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/dummy")
@RequiredArgsConstructor
@Slf4j
public class DummyDataController {

    private final DummyDataService dummyDataService;
    private final UserTypeClassificationService userTypeClassificationService;

    /**
     * 마스터 데이터 생성 (영웅, 아이템, 번들) - 최초 1회만 실행
     */
    @PostMapping("/master")
    public ResponseEntity<String> generateMasterData() {
        try {
            dummyDataService.generateMasterData();
            return ResponseEntity.ok("마스터 데이터 생성이 완료되었습니다.");
        } catch (Exception e) {
            log.error("마스터 데이터 생성 중 오류 발생", e);
            return ResponseEntity.badRequest().body("마스터 데이터 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 6개월 벌크 데이터 생성
     */
    @PostMapping("/bulk")
    public ResponseEntity<String> generateBulkData(
            @RequestParam(defaultValue = "2025-01-05") String fromDate,
            @RequestParam(defaultValue = "2025-07-05") String toDate) {
        try {
            LocalDate from = LocalDate.parse(fromDate);
            LocalDate to = LocalDate.parse(toDate);

            dummyDataService.generateBulkData(from, to);
            return ResponseEntity.ok(String.format("벌크 데이터 생성이 완료되었습니다. (%s ~ %s)", from, to));
        } catch (Exception e) {
            log.error("벌크 데이터 생성 중 오류 발생", e);
            return ResponseEntity.badRequest().body("벌크 데이터 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 일일 데이터 생성 (매일 실행)
     */
    @PostMapping("/daily")
    public ResponseEntity<String> generateDailyData(
            @RequestParam(required = false) String date) {
        try {
            LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();

            dummyDataService.generateDailyData(targetDate);
            return ResponseEntity.ok(String.format("%s 일일 데이터 생성이 완료되었습니다.", targetDate));
        } catch (Exception e) {
            log.error("일일 데이터 생성 중 오류 발생", e);
            return ResponseEntity.badRequest().body("일일 데이터 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 오늘 데이터 생성 (간편 API)
     */
    @PostMapping("/today")
    public ResponseEntity<String> generateTodayData() {
        return generateDailyData(null);
    }

    /**
     * 전체 초기화 및 재생성
     */
    @PostMapping("/reset-all")
    public ResponseEntity<String> resetAndGenerateAll() {
        try {
            log.warn("=== 전체 데이터 초기화 및 재생성 시작 ===");

            // 1. 마스터 데이터 생성
            dummyDataService.generateMasterData();

            // 2. 6개월 벌크 데이터 생성
            LocalDate from = LocalDate.now().minusMonths(6);
            LocalDate to = LocalDate.now().minusDays(1);
            dummyDataService.generateBulkData(from, to);

            // 3. 오늘 데이터 생성
            dummyDataService.generateDailyData(LocalDate.now());

            // 4. 유저 타입 분류
            userTypeClassificationService.classifyAllUsers();

            return ResponseEntity.ok("전체 데이터 초기화 및 재생성이 완료되었습니다.");
        } catch (Exception e) {
            log.error("전체 데이터 재생성 중 오류 발생", e);
            return ResponseEntity.badRequest().body("전체 데이터 재생성 실패: " + e.getMessage());
        }
    }

    /**
     * 유저 타입 분류 실행
     */
    @PostMapping("/classify-users")
    public ResponseEntity<String> classifyUsers() {
        try {
            userTypeClassificationService.classifyAllUsers();
            return ResponseEntity.ok("유저 타입 분류가 완료되었습니다.");
        } catch (Exception e) {
            log.error("유저 타입 분류 중 오류 발생", e);
            return ResponseEntity.badRequest().body("유저 타입 분류 실패: " + e.getMessage());
        }
    }
}
