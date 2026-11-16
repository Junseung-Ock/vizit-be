# @Transactional 사용 결정 기준

## 핵심 원칙

서비스 메서드에 `@Transactional`이 필요한 경우는 두 가지다.
1. **쓰기가 여러 개**라 함께 롤백돼야 할 때
2. **조회한 엔티티를 수정**해서 변경 감지(dirty checking)로 반영해야 할 때

쓰기가 `save` 하나뿐이라면 `SimpleJpaRepository.save()`에 이미 `@Transactional`이 붙어 있으므로 서비스에 굳이 달 필요 없다.

## 외부 HTTP 호출 + 트랜잭션 조합은 피해라

`@Transactional` 메서드 안에서 외부 API를 호출하면, 커넥션 풀에서 커넥션을 잡은 채로 HTTP 응답을 기다린다. 외부 API가 느리면 그 시간 동안 커넥션이 묶인다. 로그인처럼 동시 요청이 몰리는 엔드포인트에서 풀 고갈로 이어질 수 있다.

```java
// 나쁜 예 — 커넥션을 잡고 카카오 HTTP 호출 대기
@Transactional
public TokenResponse kakaoLogin(KakaoLoginRequest request) {
    KakaoUserInfoResponse userInfo = kakaoClient.getUserInfo(...); // 여기서 대기
    ...
}

// 좋은 예 — @Transactional 없음, SimpleJpaRepository가 save() 단위로 트랜잭션 관리
public TokenResponse kakaoLogin(KakaoLoginRequest request) {
    KakaoUserInfoResponse userInfo = kakaoClient.getUserInfo(...); // 트랜잭션 없음
    User user = userRepository.findByKakaoId(userInfo.getId())
            .orElseGet(() -> userRepository.save(User.create(...))); // save()가 자체 트랜잭션
    return TokenResponse.of(jwtProvider.generate(user.getId()));
}
```

## 동시 최초 가입(race condition) 처리

`findByKakaoId` → `save` 패턴은 두 요청이 동시에 들어올 경우 둘 다 조회에 실패하고 둘 다 `save`를 시도한다. 서비스 트랜잭션으로 묶어도 이 타이밍 문제는 해결되지 않는다(`SELECT FOR UPDATE` 없이는 불가).

올바른 처리: `kakaoId`에 유니크 제약을 걸어두고, 두 번째 `save`의 `DataIntegrityViolationException`을 catch해서 재조회하는 방식으로 처리한다.

## TransactionTemplate은 언제 쓰나

같은 클래스 내부에서 일부 구간만 트랜잭션으로 감싸고 싶을 때 쓴다.
(Spring의 `@Transactional`은 프록시 기반이라 같은 클래스 내부 호출은 프록시를 우회하므로 적용되지 않는다.)

실제로 필요한 케이스:
- 조건부로 트랜잭션을 열어야 할 때
- 루프 안에서 반복 커밋해야 할 때
- 트랜잭션 중간에 커밋 시점을 수동 제어해야 할 때

단순히 외부 호출을 트랜잭션 밖으로 빼고 싶다면, `@Transactional`을 제거하거나 별도 빈으로 분리하는 쪽이 훨씬 간단하다.

## 적용 사례 (AuthService)

- **이슈 #7**: `kakaoLogin`에서 `@Transactional` 제거 — 카카오 HTTP 호출이 트랜잭션 밖에서 실행되도록 수정
- **향후 추가 시 재검토**: 로그인 시 닉네임/프로필 갱신, 리프레시 토큰 저장 등이 추가되면 그때 트랜잭션 경계를 다시 설계한다.
