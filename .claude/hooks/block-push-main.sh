#!/bin/bash
# main 브랜치에 직접 푸시 차단

INPUT=$(cat)
if echo "$INPUT" | grep -qE "git push.+\bmain\b"; then
  echo "⛔ main 브랜치에 직접 푸시할 수 없습니다. develop을 통해 PR을 올리세요."
  exit 1
fi

exit 0
