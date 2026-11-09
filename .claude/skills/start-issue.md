# start-issue

새 작업을 시작할 때 호출한다. GitHub 이슈 생성부터 브랜치 생성까지 한 번에 처리한다.

## 실행 절차

1. 사용자에게 아래 정보를 확인한다.
   - 작업 제목 (이슈 제목으로 사용)
   - 작업 내용 설명 (이슈 본문으로 사용)
   - 브랜치 타입: `feature` / `fix` / `chore`

2. GitHub 이슈를 생성하고 번호를 확인한다.
   ```bash
   gh issue create --title "{제목}" --body "{내용}" --label "{feature|bug|chore}"
   ```
   출력에서 이슈 번호를 추출한다.

3. develop 브랜치로 이동한다.
   ```bash
   git checkout develop && git pull origin develop
   ```

4. 이슈 번호로 브랜치를 생성한다.
   ```bash
   git checkout -b {타입}/{이슈번호}-{제목-kebab-case}
   ```

5. 완료 후 아래를 안내한다.
   - 생성된 이슈 URL
   - 현재 브랜치명
   - 작업 완료 후 `/document-issue` 실행할 것
