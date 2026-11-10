# #7 JWT 인증 및 카카오 OAuth2 로그인 구현

- **브랜치**: feature/7-jwt-kakao-oauth
- **작업일**: 2026-11-09

## 작업 내용
> 카카오 OAuth2 로그인 후 JWT를 발급해 이후 모든 API 요청을 인증하는 구조를 구축했다.
> 모바일 앱이 카카오 SDK로 받은 액세스 토큰을 서버에 전달하면, 서버가 카카오 API로 유저 정보를 조회하고 JWT를 발급하는 방식이다.

## 변경된 파일
- `build.gradle` — spring-boot-starter-security, jjwt 0.12.6 의존성 추가
- `global/common/BaseEntity.java` — createdAt/updatedAt 공통 추상 클래스
- `global/config/JpaConfig.java` — @EnableJpaAuditing 설정
- `global/response/ApiResponse.java` — 공통 응답 래퍼
- `global/exception/ErrorCode.java` — 에러 코드 enum
- `global/exception/CustomException.java` — 커스텀 예외
- `global/exception/GlobalExceptionHandler.java` — 전역 예외 처리
- `global/security/SecurityConfig.java` — CSRF 비활성화, Stateless, 인증 필요 경로 설정
- `global/security/CustomUserDetails.java` — userId 기반 UserDetails 구현
- `global/security/CustomUserDetailsService.java` — userId로 유저 조회
- `global/security/jwt/JwtProvider.java` — JWT 생성/검증
- `global/security/jwt/JwtFilter.java` — 요청마다 Authorization 헤더에서 JWT 추출 및 검증
- `domain/user/entity/User.java` — 카카오 로그인 유저 엔티티
- `domain/user/repository/UserRepository.java` — findByKakaoId 쿼리
- `domain/auth/dto/KakaoLoginRequest.java` — 카카오 액세스 토큰 요청 DTO
- `domain/auth/dto/TokenResponse.java` — JWT 응답 DTO
- `domain/auth/service/AuthService.java` — 카카오 유저 정보 조회 → 유저 생성/조회 → JWT 발급
- `domain/auth/controller/AuthController.java` — POST /api/v1/auth/kakao
- `infra/kakao/KakaoClient.java` — 카카오 유저 정보 API 호출
- `infra/kakao/dto/KakaoUserInfoResponse.java` — 카카오 API 응답 매핑
- `application.yml` — jwt.secret, jwt.expiration-ms 환경 변수 참조 추가

## 주요 결정 사항
> - 카카오 OAuth는 모바일 SDK 방식 (서버가 auth code 교환 없이, 클라이언트가 카카오 액세스 토큰을 직접 전달)
> - JWT는 액세스 토큰만 발급 (리프레시 토큰은 추후 필요 시 추가)
> - BaseEntity로 createdAt/updatedAt 공통화 — 모든 엔티티가 상속
> - /api/v1/auth/** 경로만 permitAll, 나머지는 JWT 인증 필요

## 다음 작업 시 참고할 점
> - jwt.secret은 application-local.yml에 직접 설정 (gitignore 처리됨)
> - 리프레시 토큰 필요 시 Redis에 저장하는 방식으로 추가 예정
> - 현재 로그아웃 미구현 — 필요 시 Redis 블랙리스트 방식으로 추가
