package com.vizit.vizitapi.domain.auth.service;

import com.vizit.vizitapi.domain.auth.dto.KakaoLoginRequest;
import com.vizit.vizitapi.domain.auth.dto.TokenResponse;
import com.vizit.vizitapi.domain.user.entity.User;
import com.vizit.vizitapi.domain.user.repository.UserRepository;
import com.vizit.vizitapi.global.exception.CustomException;
import com.vizit.vizitapi.global.exception.ErrorCode;
import com.vizit.vizitapi.global.security.jwt.JwtProvider;
import com.vizit.vizitapi.infra.kakao.KakaoClient;
import com.vizit.vizitapi.infra.kakao.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public TokenResponse kakaoLogin(KakaoLoginRequest request) {
        KakaoUserInfoResponse userInfo = kakaoClient.getUserInfo(request.getAccessToken());
        User user = findOrCreateUser(userInfo);
        return TokenResponse.of(jwtProvider.generate(user.getId()));
    }

    private User findOrCreateUser(KakaoUserInfoResponse userInfo) {
        try {
            return userRepository.findByKakaoId(userInfo.getId())
                    .orElseGet(() -> createUser(userInfo));
        } catch (DataIntegrityViolationException e) {
            return userRepository.findByKakaoId(userInfo.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    private User createUser(KakaoUserInfoResponse userInfo) {
        var account = userInfo.getKakaoAccount();
        var profile = account != null ? account.getProfile() : null;
        String nickname = profile != null ? profile.getNickname() : null;
        String imageUrl = profile != null ? profile.getProfileImageUrl() : null;

        if (nickname != null) {
            return userRepository.save(User.create(userInfo.getId(), nickname, imageUrl));
        }

        // 닉네임 미동의 시 DB id 기반 기본 닉네임 생성 (두 번 저장)
        User saved = userRepository.save(User.create(userInfo.getId(), "사용자", imageUrl));
        saved.updateNickname("사용자" + saved.getId());
        return userRepository.save(saved);
    }
}
