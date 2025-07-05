package com.example.entity.dummy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.entity.dummy.repository.*;
import com.example.entity.entity.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyDataService {

    private final UserRepository userRepository;
    private final HeroesRepository heroesRepository;
    private final TierRepository tierRepository;
    private final ItemRepository itemRepository;
    private final BundleRepository bundleRepository;
    private final AccessStatsRepository accessStatsRepository;
    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;

    private final Faker faker = new Faker(Locale.KOREAN);

    // 오버워치2 영웅 데이터
    private static final Map<String, List<String>> OVERWATCH_HEROES = Map.of(
            "돌격",
            Arrays.asList("D.Va", "둠피스트", "라마트라", "라인하르트", "레킹볼", "로드호그", "마우가", "시그마", "오리사", "윈스턴", "자리야", "정커퀸",
                    "해저드"),
            "공격",
            Arrays.asList("겐지", "리퍼", "메이", "바스티온", "벤처", "소전", "솔저: 76", "솜브라", "시메트라", "애쉬", "에코", "위도우메이커", "정크랫",
                    "캐서디", "토르비욘", "트레이서", "파라", "프레야", "한조"),
            "지원", Arrays.asList("라이프위버", "루시우", "메르시", "모이라", "바티스트", "브리기테", "아나", "일리아리", "젠야타", "주노", "키리코"));

    private static final String[] TIERS = { "브론즈", "실버", "골드", "플래티넘", "다이아몬드", "마스터", "그랜드마스터" };
    private static final String[] REGIONS = { "아시아", "북미", "오세아니아", "유럽", "아프리카" };
    private static final String[] GENDERS = { "남자", "여자" };
    private static final String[] MATCH_MODES = { "일반전", "경쟁전", "아케이드" };
    private static final String[] USER_TYPES = { "신규", "복귀", "일반", "휴면", "정지", "연승", "연패", "이탈위험군" };
    private static final String[] ITEM_CATEGORIES = { "영웅스킨", "무기스킨", "하이라이트연출", "배틀패스" };
    private static final String[] SELL_TYPES = { "상시", "한정" };
    private static final String[] EVENT_NAMES = { "윈터원더랜드", "할로윈테러", "설날이벤트", "여름축제", "추석이벤트", "크리스마스축제" };
    private static final int[] ITEM_PRICES = { 10000, 15000, 20000, 25000, 30000, 35000, 40000 };

    // 마스터 데이터 생성 (1회만 실행)
    @Transactional
    public void generateMasterData() {
        log.info("=== 마스터 데이터 생성 시작 ===");

        // 1. 영웅 데이터 생성
        generateHeroes();

        // 2. 아이템 데이터 생성
        generateItems();

        // 3. 번들 아이템 생성
        generateBundleItems();

        // 4. 이벤트 아이템 생성 (시즌별)
        generateEventItems();

        log.info("=== 마스터 데이터 생성 완료 ===");
    }

    // 6개월 벌크 데이터 생성
    @Transactional
    public void generateBulkData(LocalDate from, LocalDate to) {
        log.info("=== 6개월 벌크 데이터 생성 시작: {} ~ {} ===", from, to);

        // 1. 유저 대량 생성 (점진적 증가)
        generateBulkUsers(from, to);

        // 2. 과거 데이터 생성 (날짜별)
        LocalDate current = from;
        while (!current.isAfter(to)) {
            generateDailyData(current);
            current = current.plusDays(1);
        }

        log.info("=== 6개월 벌크 데이터 생성 완료 ===");
    }

    // 일일 신규 데이터 생성
    @Transactional
    public void generateDailyData(LocalDate date) {
        log.info("=== {} 일일 데이터 생성 시작 ===", date);

        // 1. 신규 유저 생성 (주말/평일 고려)
        generateDailyUsers(date);

        // 2. 접속 로그 생성
        generateAccessStats(date);

        // 3. 매치 데이터 생성
        generateMatches(date);

        // 4. 구매 데이터 생성
        generateOrders(date);

        // 5. 환불 데이터 생성 (소량)
        generateRefunds(date);

        // 6. 티어 변동 생성
        generateTierChanges(date);

        // 7. 시즌별 신규 아이템 생성 (시즌 시작일에만)
        generateSeasonalItems(date);

        log.info("=== {} 일일 데이터 생성 완료 ===", date);
    }

    private void generateHeroes() {
        if (heroesRepository.count() > 0) {
            log.info("영웅 데이터가 이미 존재합니다. 건너뜁니다.");
            return;
        }

        OVERWATCH_HEROES.forEach((role, heroNames) -> {
            heroNames.forEach(heroName -> {
                Heroes hero = new Heroes();
                hero.setHeroesName(heroName);
                hero.setRole(role);
                heroesRepository.save(hero);
            });
        });

        log.info("영웅 데이터 생성 완료: {}개", heroesRepository.count());
    }

    private void generateItems() {
        if (itemRepository.count() > 0) {
            log.info("아이템 데이터가 이미 존재합니다. 건너뜁니다.");
            return;
        }

        List<Heroes> heroes = heroesRepository.findAll();

        // 1. 영웅 스킨 생성 (각 영웅당 5-10개)
        heroes.forEach(hero -> {
            int skinCount = faker.number().numberBetween(5, 11);
            for (int i = 0; i < skinCount; i++) {
                ItemList item = new ItemList();
                item.setItemName(generateSkinName());
                item.setItemCate("영웅스킨");
                item.setItemPrice(getRandomPrice());
                item.setSellType(getRandomSellType());
                item.setSellStartDate(LocalDate.now().minusMonths(6));
                item.setSellEndDate(item.getSellType().equals("상시") ? null : LocalDate.now().plusMonths(3));
                itemRepository.save(item);

                // heroes_item 테이블에 관계 설정
                if (hero.getItems() == null) {
                    hero.setItems(new ArrayList<>());
                }
                hero.getItems().add(item);
                heroesRepository.save(hero);
            }
        });

        // 2. 무기 스킨 생성
        heroes.forEach(hero -> {
            int weaponCount = faker.number().numberBetween(3, 8);
            for (int i = 0; i < weaponCount; i++) {
                ItemList item = new ItemList();
                item.setItemName(generateWeaponName());
                item.setItemCate("무기스킨");
                item.setItemPrice(getRandomPrice());
                item.setSellType(getRandomSellType());
                item.setSellStartDate(LocalDate.now().minusMonths(6));
                item.setSellEndDate(item.getSellType().equals("상시") ? null : LocalDate.now().plusMonths(3));
                itemRepository.save(item);

                // heroes_item 테이블에 관계 설정
                if (hero.getItems() == null) {
                    hero.setItems(new ArrayList<>());
                }
                hero.getItems().add(item);
                heroesRepository.save(hero);
            }
        });

        // 3. 하이라이트 연출 생성
        heroes.forEach(hero -> {
            int highlightCount = faker.number().numberBetween(2, 5);
            for (int i = 0; i < highlightCount; i++) {
                ItemList item = new ItemList();
                item.setItemName(generateHighlightName());
                item.setItemCate("하이라이트연출");
                item.setItemPrice(getRandomPrice());
                item.setSellType(getRandomSellType());
                item.setSellStartDate(LocalDate.now().minusMonths(6));
                item.setSellEndDate(item.getSellType().equals("상시") ? null : LocalDate.now().plusMonths(3));
                itemRepository.save(item);

                // heroes_item 테이블에 관계 설정
                if (hero.getItems() == null) {
                    hero.setItems(new ArrayList<>());
                }
                hero.getItems().add(item);
                heroesRepository.save(hero);
            }
        });

        log.info("아이템 데이터 생성 완료: {}개", itemRepository.count());
    }

    private void generateBundleItems() {
        if (bundleRepository.count() > 0) {
            log.info("번들 아이템 데이터가 이미 존재합니다. 건너뜁니다.");
            return;
        }

        List<ItemList> allItems = itemRepository.findAll();

        // 배틀패스 생성 (시즌별)
        for (int season = 1; season <= 3; season++) {
            BundleItem battlePass = new BundleItem();
            battlePass.setBundleName("시즌 " + season + " 배틀패스");
            battlePass.setBundlePrice(40000);
            battlePass.setSellType("한정");
            battlePass.setSellStartDate(LocalDate.now().minusMonths(6).plusMonths(season * 2));
            battlePass.setSellEndDate(LocalDate.now().minusMonths(6).plusMonths((season + 1) * 2));

            // 배틀패스에 랜덤 아이템 20-30개 포함
            List<ItemList> bundleItems = allItems.stream()
                    .filter(item -> !item.getItemCate().equals("배틀패스"))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                Collections.shuffle(list);
                                return list.stream().limit(faker.number().numberBetween(20, 31))
                                        .collect(Collectors.toList());
                            }));

            battlePass.setItems(bundleItems);
            bundleRepository.save(battlePass);
        }

        log.info("번들 아이템 데이터 생성 완료: {}개", bundleRepository.count());
    }

    private void generateEventItems() {
        List<Heroes> heroes = heroesRepository.findAll();
        int currentYear = LocalDate.now().getYear();

        // 최근 3년간 이벤트 아이템 생성 (2시즌마다 새로운 이벤트)
        for (int year = currentYear - 2; year <= currentYear; year++) {
            for (int season = 1; season <= 3; season += 2) { // 1, 3시즌에 이벤트 (2시즌마다)
                String eventName = EVENT_NAMES[faker.number().numberBetween(0, EVENT_NAMES.length)];
                String sellType = "이벤트(" + year + "년 " + eventName + ")";

                // 이벤트별 특별 아이템 생성
                generateEventItemsForSeason(heroes, sellType, year, season);
            }
        }

        log.info("이벤트 아이템 데이터 생성 완료");
    }

    private void generateEventItemsForSeason(List<Heroes> heroes, String sellType, int year, int season) {
        LocalDate eventStart = LocalDate.of(year, (season - 1) * 2 + 1, 1);
        LocalDate eventEnd = eventStart.plusMonths(1); // 한 달간 이벤트

        // 이벤트 전용 영웅 스킨 (인기 영웅 위주로 5-10개)
        List<Heroes> popularHeroes = heroes.stream()
                .limit(faker.number().numberBetween(5, 11))
                .collect(Collectors.toList());

        for (Heroes hero : popularHeroes) {
            ItemList eventSkin = new ItemList();
            eventSkin.setItemName(getEventSkinName(sellType));
            eventSkin.setItemCate("영웅스킨");
            eventSkin.setItemPrice(getRandomPrice()); // 이벤트 스킨
            eventSkin.setSellType(sellType);
            eventSkin.setSellStartDate(eventStart);
            eventSkin.setSellEndDate(eventEnd);
            itemRepository.save(eventSkin);

            // heroes_item 테이블에 관계 설정
            if (hero.getItems() == null) {
                hero.setItems(new ArrayList<>());
            }
            hero.getItems().add(eventSkin);
            heroesRepository.save(hero);
        }

        // 이벤트 전용 하이라이트 연출 (3-5개)
        for (int i = 0; i < faker.number().numberBetween(3, 6); i++) {
            ItemList eventHighlight = new ItemList();
            eventHighlight.setItemName(getEventHighlightName(sellType));
            eventHighlight.setItemCate("하이라이트연출");
            eventHighlight.setItemPrice(getRandomPrice());
            eventHighlight.setSellType(sellType);
            eventHighlight.setSellStartDate(eventStart);
            eventHighlight.setSellEndDate(eventEnd);
            itemRepository.save(eventHighlight);
        }
    }

    private void generateBulkUsers(LocalDate from, LocalDate to) {
        long totalDays = ChronoUnit.DAYS.between(from, to);
        int totalUsers = 10000; // 6개월간 총 1만명 생성

        // 점진적으로 유저 증가 (초기엔 적게, 후반엔 많게)
        for (int day = 0; day < totalDays; day++) {
            LocalDate currentDate = from.plusDays(day);
            int dailyUsers = (int) (totalUsers * (day + 1) / totalDays / 30); // 일일 신규 유저 수
            dailyUsers = Math.max(dailyUsers, 10); // 최소 10명

            for (int i = 0; i < dailyUsers; i++) {
                User user = createRandomUser(currentDate);
                userRepository.save(user);
            }
        }

        log.info("벌크 유저 데이터 생성 완료: {}명", userRepository.count());
    }

    private void generateDailyUsers(LocalDate date) {
        // 주말엔 더 많은 신규 유저 (평일 대비 1.5배)
        boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
        int baseUsers = faker.number().numberBetween(50, 150);
        int dailyUsers = isWeekend ? (int) (baseUsers * 1.5) : baseUsers;

        for (int i = 0; i < dailyUsers; i++) {
            User user = createRandomUser(date);
            userRepository.save(user);
        }

        log.info("{} 신규 유저 생성: {}명", date, dailyUsers);
    }

    private User createRandomUser(LocalDate joinDate) {
        User user = new User();
        user.setUserId(generateRandomEmail());
        user.setUserPw(generateRandomPassword());
        user.setUserNick(generateRandomNickname());
        user.setUserGender(GENDERS[faker.number().numberBetween(0, GENDERS.length)]);
        user.setPhone(generateRandomPhone());
        user.setRegion(REGIONS[faker.number().numberBetween(0, REGIONS.length)]);
        user.setJoinDate(joinDate);

        // 신규 유저는 90% 확률로 "신규", 10% 확률로 다른 타입 (테스트용)
        if (faker.number().numberBetween(1, 11) <= 9) {
            user.setUserType("신규");
        } else {
            user.setUserType(USER_TYPES[faker.number().numberBetween(0, USER_TYPES.length)]);
        }

        user.setVipYn(false);
        user.setReceiveYn(faker.bool().bool());
        user.setReceiveYnAt(joinDate);

        return user;
    }

    private void generateAccessStats(LocalDate date) {
        List<User> users = userRepository.findAll();

        // 각 유저별로 접속 확률 계산 (유저 타입별 차등)
        users.forEach(user -> {
            double accessProbability = calculateAccessProbability(user, date);

            if (Math.random() < accessProbability) {
                // 하루에 1-5번 접속
                int accessCount = faker.number().numberBetween(1, 6);

                for (int i = 0; i < accessCount; i++) {
                    AccessStats accessStats = new AccessStats();
                    accessStats.setUser(user);
                    accessStats.setAccessTime(generateRandomDateTime(date));
                    accessStatsRepository.save(accessStats);
                }
            }
        });
    }

    private void generateMatches(LocalDate date) {
        List<User> activeUsers = getActiveUsersForDate(date);
        List<Heroes> heroes = heroesRepository.findAll();

        // 일일 매치 수 (주말엔 더 많음)
        boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
        int dailyMatches = isWeekend ? faker.number().numberBetween(3000, 5000)
                : faker.number().numberBetween(2000, 3500);

        for (int i = 0; i < dailyMatches; i++) {
            // 매치 생성
            Match match = new Match();
            LocalDateTime startTime = generateRandomDateTime(date);
            match.setMatchStartDate(startTime);
            match.setMatchEndDate(startTime.plusMinutes(faker.number().numberBetween(5, 25))); // 5-25분 경기
            match.setMatchDate(date);
            match.setMatchMode(MATCH_MODES[faker.number().numberBetween(0, MATCH_MODES.length)]);
            match = matchRepository.save(match);

            // 매치 결과 생성 (10명 참여 - 5:5)
            List<User> matchUsers = getRandomUsers(activeUsers, 10);
            for (User user : matchUsers) {
                MatchResult result = new MatchResult();
                result.setMatch(match);
                result.setUser(user);
                result.setHeroes(getRandomHero(heroes));
                result.setVictoryOrDefeat(faker.bool().bool() ? "승리" : "패배");
                result.setMatchPlayTime(
                        (int) ChronoUnit.MINUTES.between(match.getMatchStartDate(), match.getMatchEndDate()));
                result.setPotg(faker.number().numberBetween(1, 11) == 1); // 10명 중 1명만 POTG
                matchResultRepository.save(result);
            }
        }
    }

    private void generateOrders(LocalDate date) {
        List<User> users = userRepository.findAll();
        List<ItemList> items = itemRepository.findAll();
        List<BundleItem> bundles = bundleRepository.findAll();

        // 일일 구매 건수
        int dailyOrders = faker.number().numberBetween(200, 800);

        for (int i = 0; i < dailyOrders; i++) {
            User buyer = getRandomUser(users);

            // VIP 유저는 구매 확률 높음
            double purchaseProbability = buyer.isVipYn() ? 0.8 : 0.3;

            if (Math.random() < purchaseProbability) {
                OrderList order = new OrderList();
                order.setUser(buyer);
                order.setOrderDate(date);

                // 80% 확률로 개별 아이템, 20% 확률로 번들
                if (faker.number().numberBetween(1, 11) <= 8) {
                    ItemList item = getRandomItem(items);
                    order.setItemIdx(item.getItemIdx());
                    order.setItemCate(item.getItemCate());
                } else {
                    BundleItem bundle = getRandomBundle(bundles);
                    order.setItemIdx(bundle.getBundleIdx());
                    order.setItemCate("배틀패스");
                }

                orderRepository.save(order);
            }
        }
    }

    private void generateRefunds(LocalDate date) {
        List<OrderList> recentOrders = orderRepository.findByOrderDateBetween(
                date.minusDays(30), date.minusDays(1));

        // 구매의 2-5% 정도 환불
        double refundRate = 0.02 + (0.05 - 0.02) * Math.random();
        int refundCount = (int) (recentOrders.size() * refundRate);

        Collections.shuffle(recentOrders);
        for (int i = 0; i < Math.min(refundCount, recentOrders.size()); i++) {
            OrderList order = recentOrders.get(i);

            RefundList refund = new RefundList();
            refund.setOrderList(order);
            refund.setUser(order.getUser());
            refund.setRefundDate(date);
            refund.setRefundAmt(calculateRefundAmount(order));
            refund.setWhy(generateRefundReason());

            // ItemList 설정
            if (!order.getItemCate().equals("배틀패스")) {
                ItemList item = itemRepository.findById(order.getItemIdx()).orElse(null);
                refund.setItemList(item);
            }

            refundRepository.save(refund);
        }
    }

    private void generateTierChanges(LocalDate date) {
        List<User> users = userRepository.findAll();

        // 경쟁전 플레이어들 중 일부만 티어 변동
        users.stream()
                .filter(user -> hasPlayedCompetitive(user, date))
                .forEach(user -> {
                    if (faker.number().numberBetween(1, 11) <= 3) { // 30% 확률로 티어 변동
                        Tier tier = new Tier();
                        tier.setUser(user);
                        tier.setTierName(getRandomTier());
                        tier.setTierDate(date);
                        tier.setSeason(getCurrentSeason(date));
                        tierRepository.save(tier);
                    }
                });
    }

    // === 유틸리티 메서드들 ===

    private String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    private String generateRandomPassword() {
        return faker.internet().password(8, 20);
    }

    private String generateRandomNickname() {
        // 유니크한 닉네임 생성 (최대 12자리)
        String nickname;
        do {
            nickname = generateUniqueNickname();
        } while (nickname.length() > 12 || isNicknameExists(nickname));

        return nickname;
    }

    private String generateUniqueNickname() {
        String[] prefixes = { "Pro", "Elite", "Shadow", "Cyber", "Neon", "Dark", "Fire", "Ice", "Storm", "Dragon",
                "Wolf", "Eagle", "Tiger", "Viper", "Ghost", "Blade", "Steel", "Nova", "Zeta", "Alpha" };
        String[] suffixes = { "Gamer", "Hunter", "Slayer", "Sniper", "Tank", "DPS", "Ace", "Hero", "King", "Lord",
                "Star", "Moon", "Sun", "Sky", "Wave", "Bolt", "Fury", "Rage", "Soul", "Mind" };

        // 랜덤 숫자 (1000-9999)
        int randomNum = faker.number().numberBetween(1000, 10000);

        // 50% 확률로 prefix + 숫자, 50% 확률로 prefix + suffix
        if (faker.bool().bool()) {
            String prefix = prefixes[faker.number().numberBetween(0, prefixes.length)];
            return prefix + randomNum;
        } else {
            String prefix = prefixes[faker.number().numberBetween(0, prefixes.length)];
            String suffix = suffixes[faker.number().numberBetween(0, suffixes.length)];
            // 너무 길면 숫자로 대체
            if ((prefix + suffix).length() > 12) {
                return prefix + faker.number().numberBetween(100, 1000);
            }
            return prefix + suffix;
        }
    }

    private boolean isNicknameExists(String nickname) {
        // 실제로는 DB에서 중복 체크해야 하지만, 여기서는 간단히 처리
        // 대량 데이터 생성 시 성능을 위해 확률적으로 처리
        return faker.number().numberBetween(1, 1001) == 1; // 0.1% 확률로 중복으로 간주
    }

    private String generateRandomPhone() {
        return "010-" +
                String.format("%04d", faker.number().numberBetween(1000, 10000)) + "-" +
                String.format("%04d", faker.number().numberBetween(1000, 10000));
    }

    private String generateSkinName() {
        String[] skinGrades = { "전설", "서사", "희귀", "일반", "특별", "한정", "프리미엄", "컬렉터", "마스터" };
        String[] skinThemes = { "사이버펑크", "스팀펑크", "고딕", "클래식", "미래형", "레트로", "네온", "홀로그램",
                "크리스탈", "용암", "얼음", "번개", "그림자", "빛", "우주", "기계", "자연", "바다" };
        String[] skinStyles = { "워리어", "어쌔신", "가디언", "스나이퍼", "메딕", "탱커", "스피드스터", "마법사",
                "사무라이", "닌자", "기사", "해적", "로봇", "엘프", "드래곤", "피닉스" };

        // 30% 확률로 3단어, 70% 확률로 2단어
        if (faker.number().numberBetween(1, 11) <= 3) {
            return skinGrades[faker.number().numberBetween(0, skinGrades.length)] + " " +
                    skinThemes[faker.number().numberBetween(0, skinThemes.length)] + " " +
                    skinStyles[faker.number().numberBetween(0, skinStyles.length)] + " 스킨";
        } else {
            return skinGrades[faker.number().numberBetween(0, skinGrades.length)] + " " +
                    skinThemes[faker.number().numberBetween(0, skinThemes.length)] + " 스킨";
        }
    }

    private String generateWeaponName() {
        String[] weaponMaterials = { "황금", "다이아몬드", "크롬", "카본", "네온", "플라즈마", "티타늄", "미스릴",
                "크리스탈", "용암", "얼음", "번개", "홀로그램", "레이저", "양자", "나노" };
        String[] weaponStyles = { "블레이드", "캐논", "펄스", "스파이크", "글레이브", "스피어", "해머", "소드",
                "건", "레이", "볼트", "스트림", "웨이브", "버스트", "스톰" };

        return weaponMaterials[faker.number().numberBetween(0, weaponMaterials.length)] + " " +
                weaponStyles[faker.number().numberBetween(0, weaponStyles.length)] + " 무기";
    }

    private String generateHighlightName() {
        String[] actionWords = { "승리의", "완벽한", "화려한", "극적인", "영웅적", "전설적", "환상적", "멋진",
                "놀라운", "경이로운", "장엄한", "우아한", "강력한", "신속한", "정확한" };
        String[] highlightTypes = { "함성", "조준", "연출", "순간", "행동", "기술", "콤보", "피니시", "어택",
                "세이브", "플레이", "샷", "무브", "스킬", "액션", "스트라이크" };

        return actionWords[faker.number().numberBetween(0, actionWords.length)] + " " +
                highlightTypes[faker.number().numberBetween(0, highlightTypes.length)];
    }

    private String getRandomSellType() {
        // 80% 확률로 상시/한정, 20% 확률로 이벤트
        if (faker.number().numberBetween(1, 11) <= 8) {
            return SELL_TYPES[faker.number().numberBetween(0, SELL_TYPES.length)];
        } else {
            // 이벤트 아이템 (년도 포함)
            int year = LocalDate.now().getYear() - faker.number().numberBetween(0, 3);
            String eventName = EVENT_NAMES[faker.number().numberBetween(0, EVENT_NAMES.length)];
            return "이벤트(" + year + "년 " + eventName + ")";
        }
    }

    private String getEventSkinName(String sellType) {
        String[] eventGrades = { "한정판", "특별판", "기념판", "축제", "프리미엄", "컬렉터", "이벤트", "레어" };
        String[] eventThemes = { "윈터", "할로윈", "설날", "여름", "추석", "크리스마스", "신년", "봄꽃", "가을",
                "파티", "축제", "기념", "특별", "황금", "다이아몬드" };
        String[] eventStyles = { "에디션", "컬렉션", "시리즈", "버전", "스페셜", "익스클루시브", "리미티드" };

        return eventGrades[faker.number().numberBetween(0, eventGrades.length)] + " " +
                eventThemes[faker.number().numberBetween(0, eventThemes.length)] + " " +
                eventStyles[faker.number().numberBetween(0, eventStyles.length)] + " 스킨";
    }

    private String getEventHighlightName(String sellType) {
        String[] eventActions = { "축제의", "기념", "특별한", "한정", "프리미엄", "컬렉터", "이벤트", "레어",
                "골든", "다이아몬드", "플래티넘", "마스터" };
        String[] eventMoments = { "순간", "연출", "승리", "하이라이트", "모멘트", "액션", "플레이", "피니시",
                "콤보", "스킬", "테크닉", "퍼포먼스" };

        return eventActions[faker.number().numberBetween(0, eventActions.length)] + " " +
                eventMoments[faker.number().numberBetween(0, eventMoments.length)];
    }

    private int getRandomPrice() {
        return ITEM_PRICES[faker.number().numberBetween(0, ITEM_PRICES.length)];
    }

    private void generateSeasonalItems(LocalDate date) {
        // 시즌 시작일 체크 (2개월마다 새로운 시즌)
        if (!isSeasonStartDate(date)) {
            return;
        }

        int currentSeason = getCurrentSeason(date);
        log.info("시즌 {} 신규 아이템 생성 시작", currentSeason);

        List<Heroes> heroes = heroesRepository.findAll();

        // 시즌별 신규 영웅 스킨 (5-8개)
        for (int i = 0; i < faker.number().numberBetween(5, 9); i++) {
            Heroes hero = getRandomHero(heroes);
            ItemList seasonSkin = new ItemList();
            seasonSkin.setItemName("시즌" + currentSeason + " " + generateSkinName());
            seasonSkin.setItemCate("영웅스킨");
            seasonSkin.setItemPrice(getRandomPrice());
            seasonSkin.setSellType("한정");
            seasonSkin.setSellStartDate(date);
            seasonSkin.setSellEndDate(date.plusMonths(2)); // 시즌 종료까지
            itemRepository.save(seasonSkin);

            // heroes_item 테이블에 관계 설정
            if (hero.getItems() == null) {
                hero.setItems(new ArrayList<>());
            }
            hero.getItems().add(seasonSkin);
            heroesRepository.save(hero);
        }

        // 시즌별 신규 무기 스킨 (3-5개)
        for (int i = 0; i < faker.number().numberBetween(3, 6); i++) {
            Heroes hero = getRandomHero(heroes);
            ItemList seasonWeapon = new ItemList();
            seasonWeapon.setItemName("시즌" + currentSeason + " " + generateWeaponName());
            seasonWeapon.setItemCate("무기스킨");
            seasonWeapon.setItemPrice(getRandomPrice());
            seasonWeapon.setSellType("한정");
            seasonWeapon.setSellStartDate(date);
            seasonWeapon.setSellEndDate(date.plusMonths(2));
            itemRepository.save(seasonWeapon);

            // heroes_item 테이블에 관계 설정
            if (hero.getItems() == null) {
                hero.setItems(new ArrayList<>());
            }
            hero.getItems().add(seasonWeapon);
            heroesRepository.save(hero);
        }

        // 시즌별 신규 하이라이트 연출 (2-3개)
        for (int i = 0; i < faker.number().numberBetween(2, 4); i++) {
            Heroes hero = getRandomHero(heroes);
            ItemList seasonHighlight = new ItemList();
            seasonHighlight.setItemName("시즌" + currentSeason + " " + generateHighlightName());
            seasonHighlight.setItemCate("하이라이트연출");
            seasonHighlight.setItemPrice(getRandomPrice());
            seasonHighlight.setSellType("한정");
            seasonHighlight.setSellStartDate(date);
            seasonHighlight.setSellEndDate(date.plusMonths(2));
            itemRepository.save(seasonHighlight);

            // heroes_item 테이블에 관계 설정
            if (hero.getItems() == null) {
                hero.setItems(new ArrayList<>());
            }
            hero.getItems().add(seasonHighlight);
            heroesRepository.save(hero);
        }

        log.info("시즌 {} 신규 아이템 생성 완료", currentSeason);
    }

    private boolean isSeasonStartDate(LocalDate date) {
        // 2025년 1월 1일을 기준으로 2개월마다 시즌 시작
        LocalDate baseDate = LocalDate.of(2025, 1, 1);
        long monthsDiff = ChronoUnit.MONTHS.between(baseDate, date);

        // 2개월마다 시즌 시작이고, 해당 월의 1일인 경우
        return monthsDiff % 2 == 0 && date.getDayOfMonth() == 1;
    }

    private LocalDateTime generateRandomDateTime(LocalDate date) {
        int hour = faker.number().numberBetween(0, 24);
        int minute = faker.number().numberBetween(0, 60);
        int second = faker.number().numberBetween(0, 60);
        return date.atTime(hour, minute, second);
    }

    private double calculateAccessProbability(User user, LocalDate date) {
        // 유저 타입별 접속 확률
        switch (user.getUserType()) {
            case "신규":
                return 0.8;
            case "복귀":
                return 0.7;
            case "일반":
                return 0.6;
            case "연승":
            case "연패":
                return 0.9;
            case "휴면":
                return 0.1;
            case "이탈위험군":
                return 0.2;
            default:
                return 0.5;
        }
    }

    private List<User> getActiveUsersForDate(LocalDate date) {
        // 실제로는 DB에서 해당 날짜 접속한 유저들을 가져와야 하지만,
        // 여기서는 간단히 전체 유저 중 일부를 랜덤 선택
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> Math.random() < calculateAccessProbability(user, date))
                .collect(Collectors.toList());
    }

    private List<User> getRandomUsers(List<User> users, int count) {
        Collections.shuffle(users);
        return users.stream().limit(count).collect(Collectors.toList());
    }

    private User getRandomUser(List<User> users) {
        return users.get(faker.number().numberBetween(0, users.size()));
    }

    private Heroes getRandomHero(List<Heroes> heroes) {
        return heroes.get(faker.number().numberBetween(0, heroes.size()));
    }

    private ItemList getRandomItem(List<ItemList> items) {
        return items.get(faker.number().numberBetween(0, items.size()));
    }

    private BundleItem getRandomBundle(List<BundleItem> bundles) {
        return bundles.get(faker.number().numberBetween(0, bundles.size()));
    }

    private String getRandomTier() {
        return TIERS[faker.number().numberBetween(0, TIERS.length)];
    }

    private int calculateRefundAmount(OrderList order) {
        // 전액 환불만 가능
        if (order.getItemCate().equals("배틀패스")) {
            return 40000; // 배틀패스 정가
        } else {
            ItemList item = itemRepository.findById(order.getItemIdx()).orElse(null);
            if (item != null) {
                return item.getItemPrice(); // 아이템 정가 전액 환불
            }
        }
        return 1000; // 기본값
    }

    private String generateRefundReason() {
        String[] reasons = { "단순변심", "중복구매", "게임오류", "결제오류", "품질불만", "기타" };
        return reasons[faker.number().numberBetween(0, reasons.length)];
    }

    private boolean hasPlayedCompetitive(User user, LocalDate date) {
        // 실제로는 DB에서 해당 유저의 경쟁전 플레이 여부를 확인해야 함
        return faker.bool().bool();
    }

    private int getCurrentSeason(LocalDate date) {
        // 2달마다 시즌 변경
        return (int) (ChronoUnit.MONTHS.between(LocalDate.of(2025, 1, 1), date) / 2) + 1;
    }
}
