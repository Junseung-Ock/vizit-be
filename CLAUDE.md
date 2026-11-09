# vizit-be

지역 정복 지도 앱 **Vizit**의 백엔드 API 서버.

## 프로젝트 개요

카카오맵 기반으로 방문한 장소를 기록하고 행정구역(시도/시군구)을 정복해 나가는 여행 기록 앱.
전국을 다 채우면 회차가 올라가고, 친구와 추억을 공유하며 코스를 만들어 공개할 수 있다.

## 기술 스택

- **Framework**: Spring Boot 4.0 / Java 17
- **Build**: Gradle
- **DB**: MySQL 8 (Docker)
- **Cache / 랭킹**: Redis (Docker)
- **파일 저장**: AWS S3
- **인증**: JWT + Kakao OAuth2
- **외부 API**: 카카오 Local API (장소 검색, 역지오코딩)

## 패키지 구조

```
com.vizit.vizitapi
├── domain          # 도메인별 패키지 (entity, repository, service, controller)
├── global          # 공통 (exception, response, config, security, util)
└── infra           # 외부 연동 (kakao, s3, redis)
```

## 개발 규칙

- 브랜치: `main` (배포) / `dev` (통합) / `feature/xxx` (기능)
- PR은 `dev`로, 릴리즈만 `main`으로 머지
- 커밋 컨벤션: `[FEAT]`, `[FIX]`, `[REFACTOR]`, `[DOCS]`, `[TEST]`, `[CHORE]`
- 이슈 없이 PR 올리지 않기

## 로컬 실행

```bash
# Docker로 MySQL + Redis 띄우기
docker-compose up -d

# 빌드 (테스트 제외)
./gradlew build -x test

# 실행
./gradlew bootRun
```

## 환경 변수

`application.yml`은 `local` / `prod` 프로파일로 분리.
민감한 값(DB 비밀번호, JWT 시크릿, 카카오 API 키 등)은 환경 변수로 주입하고 커밋하지 않는다.

## 주요 도메인

| 도메인 | 설명 |
|--------|------|
| User | 카카오 OAuth 로그인, 랭킹 동의, 전국일주 횟수 |
| Place | 카카오 장소 캐시 + 행정구역 코드 |
| UserPlace | 유저별 장소 저장 (WISHLIST/VISITED, 공개 범위) |
| ConquestRound | 정복 회차 관리 |
| RegionConquest | 회차별 시군구 정복 현황 |
| Memory | 추억 (글, 사진, 동행 태그) |
| Friendship | 친구 요청/수락 |
| Course | 여행 코스 (스냅샷 저장) |
| Ranking | Redis Sorted Set 기반 랭킹 |
| VerificationPhoto | 필름 카메라 GPS 인증샷 |
