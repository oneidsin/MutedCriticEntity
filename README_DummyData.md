# 🎮 오버워치2 기반 게임 CRM 더미 데이터 생성 시스템

## 📋 개요

이 시스템은 오버워치2를 모티브로 한 게임 CRM 프로젝트를 위한 현실적인 더미 데이터를 생성합니다.

## 🎯 주요 기능

### 📊 **생성되는 데이터**
- **영웅 데이터**: 오버워치2 실제 영웅 43명 (돌격/공격/지원)
- **아이템 데이터**: 영웅스킨, 무기스킨, 하이라이트연출, 배틀패스
- **시즌 아이템**: 매 시즌(2개월)마다 신규 아이템 추가
- **이벤트 아이템**: 년도별 이벤트 전용 아이템 (2시즌마다 신규)
- **유저 데이터**: 10,000-50,000명의 다양한 패턴 유저
- **게임 플레이**: 일평균 2,000-5,000 매치
- **구매 데이터**: 일평균 200-800건 구매
- **통계 데이터**: 일일/주간/월간 통계

### 🎮 **게임 특성 반영**
- **티어 시스템**: 브론즈 → 그랜드마스터 (7단계)
- **게임 모드**: 일반전/경쟁전/아케이드
- **지역별 분포**: 아시아/북미/오세아니아/유럽/아프리카
- **판매 방식**: 상시/한정/이벤트(년도별) 아이템
- **이벤트 시스템**: 2시즌마다 새로운 이벤트 진행

### 👥 **유저 타입 분류**
- **VIP**: 3시즌 연속 30만원 이상 구매
- **휴면**: 3시즌 연속 미접속
- **신규**: 가입 후 2달 이내
- **복귀**: 휴면 후 다시 접속한 지 2달 이내
- **이탈위험군**: 2시즌 연속 미접속
- **연승/연패**: 10연승 또는 10연패 유저

## 🚀 사용 방법

### 1️⃣ **최초 설정 (한 번만 실행)**

```bash
# 1. 마스터 데이터 생성 (영웅, 아이템, 번들)
POST http://localhost:8080/api/dummy/master

# 2. 6개월 벌크 데이터 생성
POST http://localhost:8080/api/dummy/bulk?fromDate=2025-01-05&toDate=2025-07-05

# 3. 유저 타입 분류
POST http://localhost:8080/api/dummy/classify-users
```

### 2️⃣ **일일 운영 (매일 실행)**

```bash
# 오늘 데이터 생성 (가장 간단)
POST http://localhost:8080/api/dummy/today

# 특정 날짜 데이터 생성
POST http://localhost:8080/api/dummy/daily?date=2025-07-06
```

### 3️⃣ **전체 리셋 (필요시)**

```bash
# 모든 데이터 초기화 후 재생성
POST http://localhost:8080/api/dummy/reset-all
```

## ⏰ 자동화 스케줄러

### 🔄 **자동 실행**
- **매일 오전 9시**: 신규 데이터 자동 생성
- **매주 일요일 오전 10시**: 데이터 검증 및 보정

### 📝 **스케줄러 설정**
```java
@Scheduled(cron = "0 0 9 * * *")  // 매일 오전 9시
public void generateDailyDataScheduled()

@Scheduled(cron = "0 0 10 * * SUN")  // 매주 일요일 오전 10시
public void weeklyDataValidation()
```

## 📈 생성 데이터 규모

### 📊 **일일 생성량**
- 신규 유저: 50-200명 (주말 1.5배)
- 게임 매치: 2,000-5,000경기
- 구매 건수: 200-800건
- 환불 건수: 구매의 2-5%
- 접속 로그: 유저당 1-5회

### 💰 **고정 가격 체계**
- 아이템 가격: 10,000원 / 15,000원 / 20,000원 / 25,000원 / 30,000원 / 35,000원 / 40,000원
- 배틀패스: 40,000원 (고정)
- 환불: 구매 가격 100% 전액 환불

## 🏗️ 시스템 구조

```
src/main/java/com/example/entity/dummy/
├── controller/
│   └── DummyDataController.java          # REST API 엔드포인트
├── service/
│   ├── DummyDataService.java             # 핵심 데이터 생성 로직
│   ├── DummyDataScheduler.java           # 자동 스케줄러
│   └── UserTypeClassificationService.java # 유저 타입 분류
└── repository/
    └── [각종 Repository 인터페이스들]
```

## 🔧 설정 파일

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    
  task:
    scheduling:
      pool:
        size: 5
```

## 📊 데이터 특징

### 🎯 **현실적인 패턴**
- 주말 vs 평일 차이 (1.5배 증가)
- 유저 타입별 행동 패턴
- VIP 유저 높은 구매율 (80% vs 30%)
- 시간대별 접속 분포

### 🎮 **게임 특성**
- 매치 시간: 5-25분 (현실적)
- 10명 참여 매치 (5:5 시스템)
- POTG 확률: 1/10
- 역할별 영웅 분포

### 💼 **비즈니스 로직**
- 환불률: 2-5% (현실적)
- 환불 방식: 전액 환불만 가능
- 시즌별 티어 변동
- 이벤트 아이템 한정 판매 (년도별 구분)
- 번들 상품 할인 효과
- 이벤트 주기: 2시즌(4개월)마다 신규 이벤트

## 🚨 주의사항

1. **대량 데이터 생성**: 벌크 데이터 생성 시 시간이 오래 걸릴 수 있습니다.
2. **메모리 사용량**: 대량 데이터 처리 시 메모리 사용량이 증가합니다.
3. **DB 성능**: 인덱스 설정을 권장합니다.
4. **스케줄러**: 운영 환경에서는 스케줄러 시간 조정이 필요할 수 있습니다.

## 🔍 트러블슈팅

### 자주 발생하는 문제들

1. **메모리 부족**
   ```bash
   # JVM 옵션 추가
   -Xmx2g -Xms1g
   ```

2. **DB 연결 타임아웃**
   ```yaml
   spring:
     datasource:
       hikari:
         maximum-pool-size: 20
         connection-timeout: 30000
   ```

3. **스케줄러 미동작**
   ```java
   @SpringBootApplication
   @EnableScheduling  // 이 어노테이션 확인
   public class EntityApplication
   ```

## 📞 문의사항

프로젝트 관련 문의사항이 있으시면 언제든 연락주세요! 🎮✨ 