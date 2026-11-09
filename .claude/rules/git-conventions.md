# Git Conventions

## 브랜치 전략

```
main        # 배포용. 직접 커밋 금지. develop에서만 머지.
develop         # 통합 브랜치. feature/* 머지 대상.
feature/*   # 기능 개발. 이슈 번호 포함.
fix/*       # 버그 수정.
chore/*     # 설정, 의존성 등 코드 외 작업.
```

예시: `feature/13-user-oauth`, `fix/27-conquest-recalculate`

## 커밋 컨벤션

```
[TYPE] 제목 (50자 이내)
```

| 타입 | 용도 |
|------|------|
| FEAT | 새 기능 |
| FIX | 버그 수정 |
| REFACTOR | 기능 변경 없는 코드 개선 |
| TEST | 테스트 추가/수정 |
| DOCS | 문서 수정 |
| CHORE | 빌드, 설정, 의존성 등 |

예시: `[FEAT] 카카오 OAuth 로그인 구현`

커밋 메시지는 한글로 작성한다.

## PR 규칙

- PR 대상 브랜치는 `develop`.
- 이슈 없이 PR 올리지 않는다.
- PR 제목도 커밋 컨벤션과 동일한 형식 사용.
- Self-merge 금지 (혼자 작업 시 하루 이상 지난 뒤 머지).
