# #7 JWT 인증 및 카카오 OAuth2 로그인 구현

- **브랜치**: feature/7-jwt-kakao-oauth
- **작업일**: 2026-11-09

## 작업 내용
> 카카오 OAuth2 로그인 후 JWT를 발급해 이후 모든 API 요청을 인증하는 구조를 구축했다.
> 모바일 앱이 카카오 SDK로 받은 액세스 토큰을 서버에 전달하면, 서버가 카카오 API로 유저 정보를 조회하고 JWT를 발급하는 방식이다.

## 변경된 파일
- `build.gradle` — spring-boot-starter-security, jjwt 0.12.6 의존성 추가 / jjwt-jackson 컴파일 의존성으로 변경
- `global/common/BaseEntity.java` — createdAt/updatedAt 공통 추상 클래스
- `global/config/JpaConfig.java` — @EnableJpaAuditing 설정
- `global/config/WebConfig.java` — ObjectMapper 빈 등록
- `global/response/ApiResponse.java` — 공통 응답 래퍼
- `global/exception/ErrorCode.java` — 에러 코드 enum / 네이밍 컨벤션 통일, AUTH_KAKAO_API_FAILED 502로 수정
- `global/exception/CustomException.java` — 커스텀 예외 / cause 보존 추가
- `global/exception/GlobalExceptionHandler.java` — 전역 예외 처리 / 잘못된 요청 본문 400 처리 및 미처리 예외 로깅 추가
- `global/security/SecurityConfig.java` — CSRF 비활성화, Stateless, 인증 필요 경로 설정 / permitAll 경로 /api/v1/auth/kakao로 축소, EntryPoint 연결
- `global/security/JwtAuthenticationEntryPoint.java` — 인증 실패 시 ApiResponse 형식으로 401 반환
- `global/security/CustomUserDetails.java` — userId 기반 UserDetails 구현
- `global/security/CustomUserDetailsService.java` — userId로 유저 조회 / findById → existsById로 변경
- `global/security/jwt/JwtProvider.java` — JWT 생성/검증 / 서명 키를 생성자에서 1회 초기화하여 재사용
- `global/security/jwt/JwtFilter.java` — 요청마다 Authorization 헤더에서 JWT 추출 및 검증 / 유저 조회 실패 시 401 위임, extractUserId 재파싱 JwtException 처리 추가
- `domain/user/entity/User.java` — 카카오 로그인 유저 엔티티
- `domain/user/repository/UserRepository.java` — findByKakaoId 쿼리
- `domain/auth/dto/KakaoLoginRequest.java` — 카카오 액세스 토큰 요청 DTO
- `domain/auth/dto/TokenResponse.java` — JWT 응답 DTO
- `domain/auth/service/AuthService.java` — 카카오 유저 정보 조회 → 유저 생성/조회 → JWT 발급 / 닉네임 생성 시 두 save를 TransactionTemplate으로 원자적 처리
- `domain/auth/controller/AuthController.java` — POST /api/v1/auth/kakao
- `infra/kakao/KakaoClient.java` — 카카오 유저 정보 API 호출 / 연결·읽기 타임아웃 설정, 예외 세분화 및 cause 보존
- `infra/kakao/dto/KakaoUserInfoResponse.java` — 카카오 API 응답 매핑
- `application.yml` — jwt.secret, jwt.expiration-ms, kakao.api.base-url 환경 변수 참조 추가
- `src/test/resources/application-test.yml` — 테스트용 JWT 시크릿 (유효한 Base64 형식)

## 주요 결정 사항
> - 카카오 OAuth는 모바일 SDK 방식 (서버가 auth code 교환 없이, 클라이언트가 카카오 액세스 토큰을 직접 전달)
> - JWT는 액세스 토큰만 발급 (리프레시 토큰은 추후 필요 시 추가)
> - BaseEntity로 createdAt/updatedAt 공통화 — 모든 엔티티가 상속
> - /api/v1/auth/kakao 경로만 permitAll, 나머지는 JWT 인증 필요 (/api/v1/auth/** 대신 최소 범위로 축소)
> - JwtAuthenticationEntryPoint 추가 — 인증 실패 응답을 ApiResponse 형식으로 통일
> - KakaoClient에 연결/읽기 타임아웃 설정, baseUrl을 application.yml로 분리
> - ErrorCode 네이밍 컨벤션: 도메인 없는 공통 에러는 COMMON_ 접두사 사용 (예: COMMON_INTERNAL_SERVER_ERROR)
> - 카카오 API 에러 구분: 카카오 4xx(잘못된 토큰 등) → AUTH_INVALID_TOKEN(HTTP 401), 카카오 5xx·연결 실패 → AUTH_KAKAO_API_FAILED(HTTP 502)

## 다음 작업 시 참고할 점
> - jwt.secret은 application-local.yml에 직접 설정 (gitignore 처리됨)
> - 리프레시 토큰 필요 시 Redis에 저장하는 방식으로 추가 예정
> - 현재 로그아웃 미구현 — 필요 시 Redis 블랙리스트 방식으로 추가
