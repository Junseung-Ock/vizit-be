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

## 작업 흐름

1. `/start-issue` 호출 → 이슈 생성 + `develop` 최신화 + 브랜치 생성 자동 처리
2. 코드 작업
3. `/document-issue` 호출 → `.claude/work-logs/`에 작업 문서 자동 생성
4. 작업 문서 포함해서 커밋 후 `develop`으로 PR

관련 도메인 작업 시 `.claude/work-logs/`의 문서를 먼저 참고한다.

## 규칙 파일

상황에 맞는 규칙 파일을 반드시 먼저 읽고 작업한다.

| 파일 | 읽어야 할 때 |
|------|------------|
| `.claude/rules/code-style.md` | 새 클래스/도메인 생성, 코드 작성 전 |
| `.claude/rules/api-conventions.md` | API 엔드포인트 추가, 응답 형식 결정, 에러 처리 |
| `.claude/rules/git-conventions.md` | 커밋 메시지 작성, 브랜치 생성, PR 올리기 전 |

## 초기 설정

클론 후 아래 명령어를 한 번 실행해 git hook을 활성화한다.

```bash
git config core.hooksPath .githooks
```

hook 목록:
- `post-checkout`: `main`에서 브랜치를 따면 차단. feature/* 는 반드시 `develop`에서 딸 것.

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
