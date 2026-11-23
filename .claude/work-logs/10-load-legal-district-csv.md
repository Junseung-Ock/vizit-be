# #10 법정동 코드 CSV 적재

- **브랜치**: feature/10-load-legal-district-csv
- **작업일**: 2026-11-23

## 작업 내용
> 행정안전부 법정동 코드 TXT 파일(53,403건)을 파싱해 DB에 적재하는 구조를 구축했다.
> 장소 저장 시 카카오 역지오코딩 API로 받은 법정동 코드를 DB에서 조회해 행정구역을 특정하기 위한 핵심 선행 작업이다.
> 시도/시군구/읍면동 단위 정복 기능을 고려해 각 계층 코드를 컬럼으로 분리했다.

## 변경된 파일
- `domain/region/entity/LegalDistrict.java` — 법정동 코드 엔티티 (code, sidoCode, sigunguCode, dongCode, sidoName, sigunguName, dongName, active)
- `domain/region/repository/LegalDistrictRepository.java` — 기본 JPA Repository
- `domain/region/service/LegalDistrictLoader.java` — 앱 시작 시 TXT 파싱 → 1,000건 단위 배치 적재 (ApplicationRunner)
- `global/config/WebConfig.java` — RestClient.Builder 빈 등록 추가
- `application.yml` — hibernate.jdbc.batch_size: 1000, order_inserts: true 추가
- `docker-compose.yml` — MySQL utf8mb4 charset 설정 추가
- `src/main/resources/legal-district/legal-district.txt` — 행정안전부 법정동 코드 원본 파일

## 주요 결정 사항
> - 코드 계층 구조: sidoCode(2자리), sigunguCode(5자리), dongCode(8자리)를 모두 누적 prefix로 저장 — `WHERE sigungu_code = '11680'` 한 조건으로 조회 가능
> - 시군구 단위뿐 아니라 시도/동 단위 정복도 고려해 세 계층 코드 모두 컬럼으로 분리
> - 로더 멱등성: 앱 시작 시 `count() > 0` 확인 후 데이터 있으면 스킵 — 재실행 안전
> - 배치 적재: 1,000건 단위 saveAll + hibernate batch_size 설정으로 53,000건을 약 53회 INSERT로 처리
> - active 필드: 폐지여부 "존재"/"폐지"를 boolean으로 저장 — 폐지된 행정구역도 보관
> - 법정동명 파싱: `fullName.split(" ", 3)` — 공백 기준 최대 3분할 (시도 / 시군구 / 읍면동)

## 다음 작업 시 참고할 점
> - MySQL CLI 접속 시 `--default-character-set=utf8mb4` 옵션 필요 (없으면 한글 `?`로 표시)
> - `application-local.yml` JDBC URL에 `connectionCollation=utf8mb4_unicode_ci` 추가 필요 (gitignore 처리됨)
> - 장소 저장 기능 구현 시 카카오 역지오코딩 결과 코드로 `LegalDistrictRepository.findById(code)` 조회
> - 정복 계산 시 sidoCode/sigunguCode/dongCode 컬럼으로 그룹핑 가능
