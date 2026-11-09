#!/bin/bash
# main 또는 develop 브랜치에서 직접 코드 수정 차단

CURRENT_BRANCH=$(git symbolic-ref --short HEAD 2>/dev/null)

if [ "$CURRENT_BRANCH" = "main" ] || [ "$CURRENT_BRANCH" = "develop" ]; then
  echo "⛔ ${CURRENT_BRANCH} 브랜치에서 직접 작업할 수 없습니다."
  echo "   /start-issue 를 실행해 이슈와 브랜치를 먼저 생성하세요."
  exit 1
fi

exit 0
