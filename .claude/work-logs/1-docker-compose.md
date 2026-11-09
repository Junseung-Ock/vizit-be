# #1 docker-compose

- **브랜치**: chore/1-docker-compose
- **작업일**: 2026-11-09

## 작업 내용
> 로컬 개발환경 구성을 위해 Docker Compose로 MySQL 8과 Redis를 띄울 수 있도록 설정했다.
> application.properties를 application.yml로 전환하고 local/prod 프로파일을 분리해 환경별 설정을 관리할 수 있게 했다.

## 변경된 파일
- `docker-compose.yml` — MySQL 8 + Redis 7.2 서비스 정의, healthcheck 포함
- `src/main/resources/application.yml` — 기존 properties 대체, 활성 프로파일 `local` 기본값 설정
- `src/main/resources/application-local.yml` — 로컬 DB/Redis 연결 설정, JPA show-sql, DEBUG 로그
- `.env.example` — 필요한 환경 변수 목록 (DB, JWT, Kakao, S3)
- `build.gradle` — `spring-boot-starter-data-redis` 의존성 추가
- `.gitignore` — `.env`, `application-local.yml`, `application-prod.yml` 커밋 제외 추가

## 주요 결정 사항
> - MySQL 포트는 표준 3306 유지 (동시에 여러 프로젝트 띄우지 않는 전제)
> - `application-local.yml`은 gitignore에 추가 — DB 비밀번호 등 민감값 포함하므로 커밋하지 않음
> - `ddl-auto: update`는 로컬 개발 편의용. prod 프로파일에서는 반드시 `validate` 또는 `none`으로 변경 필요

## 다음 작업 시 참고할 점
> - 실행 전 `.env.example`을 복사해 `.env` 파일 생성 후 실제 값 입력 필요
> - `docker-compose up -d` 후 `./gradlew bootRun` 순서로 실행
> - prod 프로파일(`application-prod.yml`)은 Phase 1 배포 설정 작업 시 추가
