#!/bin/bash
# git commit 실행 전 application*.yml, .env 에 하드코딩된 민감 정보 검사

INPUT=$(cat)
COMMAND=$(echo "$INPUT" | python3 -c "import sys,json; print(json.load(sys.stdin).get('command',''))" 2>/dev/null)

echo "$COMMAND" | grep -q "git commit" || exit 0

SECRET_FOUND=0
FILES="src/main/resources/application.yml src/main/resources/application-local.yml src/main/resources/application-prod.yml .env"

for file in $FILES; do
  [ -f "$file" ] || continue
  if grep -qiE "(password|secret|api.?key)\s*:\s*['\"]?[^$\{\s'\"][^'\"\s]{3,}" "$file"; then
    echo "⛔ [$file] 하드코딩된 민감 정보가 감지됐습니다. 환경 변수로 교체 후 커밋하세요."
    SECRET_FOUND=1
  fi
done

[ "$SECRET_FOUND" -eq 1 ] && exit 1
exit 0
