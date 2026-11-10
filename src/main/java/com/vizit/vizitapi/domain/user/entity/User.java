package com.vizit.vizitapi.domain.user.entity;

import com.vizit.vizitapi.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoId;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    private boolean rankingAgreed = false;

    @Column(nullable = false)
    private int roundCount = 0;

    public static User create(Long kakaoId, String nickname, String profileImageUrl) {
        User user = new User();
        user.kakaoId = kakaoId;
        user.nickname = nickname;
        user.profileImageUrl = profileImageUrl;
        return user;
    }
}
