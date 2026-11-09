# API Conventions

## 공통 응답 형식

모든 API는 아래 형식으로 응답한다.

```json
{
  "success": true,
  "data": { ... },
  "message": null
}
```

```json
{
  "success": false,
  "data": null,
  "message": "에러 메시지"
}
```

Java 클래스: `global.response.ApiResponse<T>`

## URL 규칙

- 소문자 케밥케이스: `/user-places`, `/conquest-rounds`
- 복수 명사 사용
- 버전 prefix: `/api/v1/...`
- 리소스 중첩은 2단계까지: `/api/v1/memories/{memoryId}/photos`

## HTTP 메서드

| 메서드 | 용도 | 성공 상태코드 |
|--------|------|--------------|
| GET | 조회 | 200 |
| POST | 생성 | 201 |
| PATCH | 부분 수정 | 200 |
| DELETE | 삭제 | 204 |

전체 수정(`PUT`)은 사용하지 않는다.

## 에러 코드

`global.exception.ErrorCode` enum으로 관리.

```
{도메인}_{설명} 형식
예) USER_NOT_FOUND, PLACE_ACCESS_DENIED, MEMORY_TAG_ALREADY_EXISTS
```

| HTTP 상태코드 | 용도 |
|--------------|------|
| 400 | 잘못된 요청 (유효성 검사 실패 등) |
| 401 | 인증 실패 (토큰 없음/만료) |
| 403 | 권한 없음 (인증은 됐지만 접근 불가) |
| 404 | 리소스 없음 |
| 409 | 중복/충돌 |
| 500 | 서버 에러 |

## 페이지네이션

목록 조회는 커서 기반 페이지네이션 사용.

```json
{
  "success": true,
  "data": {
    "contents": [ ... ],
    "nextCursor": "커서값",
    "hasNext": true
  }
}
```
