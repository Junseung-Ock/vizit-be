package com.vizit.vizitapi.domain.region.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "legal_district")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LegalDistrict {

    @Id
    @Column(length = 10)
    private String code;

    @Column(nullable = false, length = 2)
    private String sidoCode;

    @Column(nullable = false, length = 5)
    private String sigunguCode;

    @Column(nullable = false, length = 8)
    private String dongCode;

    @Column(nullable = false)
    private String sidoName;

    @Column
    private String sigunguName;

    @Column
    private String dongName;

    @Column(nullable = false)
    private boolean active;

    public static LegalDistrict of(String code, String sidoName, String sigunguName,
                                   String dongName, boolean active) {
        LegalDistrict district = new LegalDistrict();
        district.code = code;
        district.sidoCode = code.substring(0, 2);
        district.sigunguCode = code.substring(0, 5);
        district.dongCode = code.substring(0, 8);
        district.sidoName = sidoName;
        district.sigunguName = sigunguName;
        district.dongName = dongName;
        district.active = active;
        return district;
    }
}
