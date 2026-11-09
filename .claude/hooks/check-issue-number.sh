#!/bin/bash
# 새 브랜치 생성 시 이슈 번호 포함 여부 검사

INPUT=$(cat)
COMMAND=$(echo "$INPUT" | python3 -c "import sys,json; print(json.load(sys.stdin).get('command',''))" 2>/dev/null)

# git checkout -b 또는 git switch -c 감지
if echo "$COMMAND" | grep -qE "git (checkout -b|switch -c)"; then
  BRANCH=$(echo "$COMMAND" | grep -oE "(feature|fix|chore)/\S+" | head -1)

  # feature/fix/chore 브랜치인데 이슈 번호(숫자-)가 없으면 차단
  if [ -n "$BRANCH" ] && ! echo "$BRANCH" | grep -qE "(feature|fix|chore)/[0-9]+-"; then
    echo "⛔ 브랜치명에 이슈 번호가 없습니다."
    echo "   형식: feature/{이슈번호}-{설명}  예) feature/13-kakao-oauth"
    echo "   GitHub에서 이슈를 먼저 생성하고 번호를 확인하세요."
    exit 1
  fi
fi

exit 0
