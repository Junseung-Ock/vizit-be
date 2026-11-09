# Code Style

## 패키지 구조

```
com.vizit.vizitapi
├── domain
│   └── {도메인명}
│       ├── entity
│       ├── repository
│       ├── service
│       └── controller
├── global
│   ├── exception
│   ├── response
│   ├── config
│   ├── security
│   └── util
└── infra
    ├── kakao
    ├── s3
    └── redis
```

## 레이어 규칙

- Controller는 요청/응답 변환만. 비즈니스 로직 금지.
- Service는 트랜잭션 단위. 다른 도메인 Service를 직접 주입하지 않고 Repository를 통해 접근.
- Repository는 JPA Repository만. 쿼리 로직은 Repository에서만 작성.
- Entity는 setter 금지. 생성자 또는 정적 팩토리 메서드(`of`, `create`) 사용.

## 네이밍

| 대상 | 규칙 | 예시 |
|------|------|------|
| Entity | 단수 명사 | `UserPlace`, `Memory` |
| Repository | `{Entity}Repository` | `UserPlaceRepository` |
| Service | `{Domain}Service` | `UserPlaceService` |
| Controller | `{Domain}Controller` | `UserPlaceController` |
| DTO (요청) | `{Action}{Domain}Request` | `CreateUserPlaceRequest` |
| DTO (응답) | `{Domain}Response` | `UserPlaceResponse` |
| Enum | 대문자 스네이크 | `WISHLIST`, `PUBLIC` |

## 기타

- Lombok `@RequiredArgsConstructor` + `final` 필드로 의존성 주입.
- `@Transactional(readOnly = true)` 기본, 쓰기 작업만 `@Transactional`.
- 주석은 WHY가 명확할 때만. 코드로 의도를 표현할 것.
