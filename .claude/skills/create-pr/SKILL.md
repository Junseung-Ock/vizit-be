---
name: create-pr
description: PR 템플릿 기반으로 develop 대상 PR 생성
---

작업이 완료된 feature/fix/chore 브랜치에서 호출한다. PR 템플릿을 읽어 본문을 작성하고 develop으로 PR을 생성한다.

## 실행 절차

1. 현재 브랜치와 develop 대비 커밋 로그를 확인한다.
   ```bash
   git symbolic-ref --short HEAD
   git log develop..HEAD --oneline
   git diff develop..HEAD --name-only
   ```

2. `.github/PULL_REQUEST_TEMPLATE.md`를 읽어 PR 본문 형식을 확인한다.

3. 커밋 로그와 변경 파일을 바탕으로 템플릿에 맞게 PR 본문을 작성한다.
   - `close #` 뒤에는 브랜치명에서 추출한 이슈 번호를 넣는다.
   - `## 작업 내용`은 커밋 로그 기반으로 핵심 작업만 bullet로 정리한다.
   - `## 테스트` 체크박스는 그대로 유지한다.
   - `## 참고 사항`은 특이사항이 없으면 비워둔다.

4. PR 제목은 git-conventions.md 커밋 컨벤션과 동일한 형식으로 작성한다.
   ```
   [TYPE] 제목
   ```

5. PR을 생성한다.
   ```bash
   gh pr create --title "{제목}" --base develop --body "{본문}"
   ```

6. 완료 후 아래를 안내한다.
   - 생성된 PR URL
   - Self-merge 금지 (하루 이상 지난 뒤 머지)
