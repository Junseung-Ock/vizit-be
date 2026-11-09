#!/bin/bash
# git push 전 현재 이슈에 대한 work-log 문서 존재 여부 확인

INPUT=$(cat)
COMMAND=$(echo "$INPUT" | python3 -c "import sys,json; print(json.load(sys.stdin).get('command',''))" 2>/dev/null)

echo "$COMMAND" | grep -q "git push" || exit 0

BRANCH=$(git symbolic-ref --short HEAD 2>/dev/null)

# feature/fix/chore 브랜치일 때만 검사
echo "$BRANCH" | grep -qE "^(feature|fix|chore)/" || exit 0

# 브랜치명에서 이슈 번호 추출 (feature/13-kakao-oauth → 13)
ISSUE_NUM=$(echo "$BRANCH" | grep -oE "[0-9]+" | head -1)
[ -z "$ISSUE_NUM" ] && exit 0

# work-log 문서 존재 여부 확인
if ! ls .claude/work-logs/${ISSUE_NUM}-*.md 2>/dev/null | grep -q .; then
  echo "⛔ 이슈 #${ISSUE_NUM}에 대한 작업 문서가 없습니다."
  echo "   푸시 전 /document-issue 를 실행해 작업 내용을 정리하세요."
  exit 1
fi

exit 0
