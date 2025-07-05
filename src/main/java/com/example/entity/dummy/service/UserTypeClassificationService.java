package com.example.entity.dummy.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.dummy.repository.*;
import com.example.entity.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTypeClassificationService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AccessStatsRepository accessStatsRepository;
    private final MatchResultRepository matchResultRepository;

    /**
     * 모든 유저의 타입을 재분류
     */
    public void classifyAllUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            String newType = classifyUser(user);
            if (!newType.equals(user.getUserType())) {
                user.setUserType(newType);
                userRepository.save(user);
                log.debug("사용자 {} 타입 변경: {} -> {}", user.getUserId(), user.getUserType(), newType);
            }
        }

        log.info("사용자 타입 분류 완료: 총 {}명", users.size());
    }

    /**
     * 개별 유저 타입 분류
     */
    public String classifyUser(User user) {
        LocalDate now = LocalDate.now();
        LocalDate joinDate = user.getJoinDate();

        // 1. 신규 유저 (가입 후 2달 이내)
        if (ChronoUnit.MONTHS.between(joinDate, now) < 2) {
            return "신규";
        }

        // 2. VIP 유저 (3시즌 연속 30만원 이상)
        if (isVipUser(user, now)) {
            user.setVipYn(true);
            return "VIP";
        }

        // 3. 휴면 유저 (3시즌 연속 미접속)
        if (isDormantUser(user, now)) {
            user.setDormantDate(now);
            return "휴면";
        }

        // 4. 복귀 유저 (휴면 후 다시 접속한 지 2달 이내)
        if (isReturningUser(user, now)) {
            return "복귀";
        }

        // 5. 이탈 위험군 (2시즌 연속 미접속)
        if (isChurnRiskUser(user, now)) {
            return "이탈위험군";
        }

        // 6. 연승/연패 유저 (10연승 또는 10연패)
        String streakType = getStreakType(user);
        if (streakType != null) {
            return streakType;
        }

        // 7. 기본값
        return "일반";
    }

    private boolean isVipUser(User user, LocalDate now) {
        // 최근 3시즌(6개월) 동안 각 시즌마다 30만원 이상 구매
        for (int season = 0; season < 3; season++) {
            LocalDate seasonStart = now.minusMonths((season + 1) * 2);
            LocalDate seasonEnd = now.minusMonths(season * 2);

            // 해당 시즌 구매 금액 계산 (실제로는 DB 쿼리 필요)
            int seasonPurchase = calculateSeasonPurchase(user, seasonStart, seasonEnd);

            if (seasonPurchase < 300000) {
                return false;
            }
        }
        return true;
    }

    private boolean isDormantUser(User user, LocalDate now) {
        // 최근 3시즌(6개월) 동안 접속 기록이 없는지 확인
        LocalDate sixMonthsAgo = now.minusMonths(6);

        // 실제로는 AccessStats 테이블에서 확인해야 함
        return getLastAccessDate(user).isBefore(sixMonthsAgo);
    }

    private boolean isReturningUser(User user, LocalDate now) {
        // 휴면 상태였다가 최근 2달 내에 다시 접속한 경우
        LocalDate dormantDate = user.getDormantDate();
        if (dormantDate == null) {
            return false;
        }

        LocalDate lastAccess = getLastAccessDate(user);
        return lastAccess.isAfter(dormantDate) &&
                ChronoUnit.MONTHS.between(lastAccess, now) < 2;
    }

    private boolean isChurnRiskUser(User user, LocalDate now) {
        // 최근 2시즌(4개월) 동안 접속 기록이 없는지 확인
        LocalDate fourMonthsAgo = now.minusMonths(4);

        return getLastAccessDate(user).isBefore(fourMonthsAgo);
    }

    private String getStreakType(User user) {
        // 최근 10경기 결과 확인
        List<String> recentResults = getRecentMatchResults(user, 10);

        if (recentResults.size() < 10) {
            return null;
        }

        // 연승 체크
        boolean isWinStreak = recentResults.stream().allMatch(result -> "승리".equals(result));
        if (isWinStreak) {
            return "연승";
        }

        // 연패 체크
        boolean isLoseStreak = recentResults.stream().allMatch(result -> "패배".equals(result));
        if (isLoseStreak) {
            return "연패";
        }

        return null;
    }

    // === 헬퍼 메서드들 (실제로는 Repository 쿼리로 구현) ===

    private int calculateSeasonPurchase(User user, LocalDate startDate, LocalDate endDate) {
        // 실제 구현에서는 OrderRepository에서 해당 기간 구매 금액 합계 조회
        // 여기서는 더미 데이터를 위한 랜덤 값 반환
        return (int) (Math.random() * 500000); // 0~50만원 랜덤
    }

    private LocalDate getLastAccessDate(User user) {
        // 실제 구현에서는 AccessStatsRepository에서 최근 접속일 조회
        // 여기서는 더미 데이터를 위한 랜덤 값 반환
        return LocalDate.now().minusDays((int) (Math.random() * 200)); // 0~200일 전 랜덤
    }

    private List<String> getRecentMatchResults(User user, int limit) {
        // 실제 구현에서는 MatchResultRepository에서 최근 경기 결과 조회
        // 여기서는 더미 데이터를 위한 랜덤 값 반환
        return List.of("승리", "패배", "승리", "승리", "패배", "승리", "패배", "승리", "승리", "패배");
    }
}