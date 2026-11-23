package com.vizit.vizitapi.domain.region.service;

import com.vizit.vizitapi.domain.region.entity.LegalDistrict;
import com.vizit.vizitapi.domain.region.repository.LegalDistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LegalDistrictLoader implements ApplicationRunner {

    private static final String CSV_PATH = "legal-district/legal-district.txt";
    private static final int BATCH_SIZE = 1000;

    private final LegalDistrictRepository legalDistrictRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (legalDistrictRepository.count() > 0) {
            log.info("법정동 코드 데이터가 이미 존재합니다. 적재를 건너뜁니다.");
            return;
        }

        log.info("법정동 코드 데이터 적재를 시작합니다.");
        long startTime = System.currentTimeMillis();

        ClassPathResource resource = new ClassPathResource(CSV_PATH);
        List<LegalDistrict> batch = new ArrayList<>(BATCH_SIZE);
        int total = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine(); // 헤더 스킵

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\t");
                if (columns.length < 3) continue;

                String code = columns[0].trim();
                String fullName = columns[1].trim();
                boolean active = "존재".equals(columns[2].trim());

                String[] nameParts = fullName.split(" ", 3);
                String sidoName = nameParts[0];
                String sigunguName = nameParts.length >= 2 ? nameParts[1] : null;
                String dongName = nameParts.length >= 3 ? nameParts[2] : null;

                batch.add(LegalDistrict.of(code, sidoName, sigunguName, dongName, active));

                if (batch.size() == BATCH_SIZE) {
                    legalDistrictRepository.saveAll(batch);
                    total += batch.size();
                    batch.clear();
                }
            }
        }

        if (!batch.isEmpty()) {
            legalDistrictRepository.saveAll(batch);
            total += batch.size();
        }

        log.info("법정동 코드 데이터 적재 완료. 총 {}건, 소요 시간: {}ms",
                total, System.currentTimeMillis() - startTime);
    }
}
