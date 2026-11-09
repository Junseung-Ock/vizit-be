#!/bin/bash
# 새 브랜치 생성 시 이슈 번호 포함 여부 및 GitHub 이슈 존재 여부 검사

INPUT=$(cat)
echo "$INPUT" | grep -qE "git (checkout -b|switch -c)" || exit 0

BRANCH=$(echo "$INPUT" | grep -oE "(feature|fix|chore)/\S+" | head -1)
[ -z "$BRANCH" ] && exit 0

# 이슈 번호 형식 검사
if ! echo "$BRANCH" | grep -qE "(feature|fix|chore)/[0-9]+-"; then
  echo "⛔ 브랜치명에 이슈 번호가 없습니다."
  echo "   형식: feature/{이슈번호}-{설명}  예) feature/13-kakao-oauth"
  echo "   작업 시작 전 /start-issue 를 실행하세요."
  exit 1
fi

# GitHub 이슈 존재 여부 검사
ISSUE_NUM=$(echo "$BRANCH" | grep -oE "[0-9]+" | head -1)
if ! gh issue view "$ISSUE_NUM" --json number -q .number >/dev/null 2>&1; then
  echo "⛔ GitHub에 이슈 #${ISSUE_NUM}이 존재하지 않습니다."
  echo "   작업 시작 전 /start-issue 를 실행해 이슈를 먼저 생성하세요."
  exit 1
fi

exit 0
