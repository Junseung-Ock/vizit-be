package com.vizit.vizitapi.domain.auth.service;

import com.vizit.vizitapi.domain.auth.dto.KakaoLoginRequest;
import com.vizit.vizitapi.domain.auth.dto.TokenResponse;
import com.vizit.vizitapi.domain.user.entity.User;
import com.vizit.vizitapi.domain.user.repository.UserRepository;
import com.vizit.vizitapi.global.security.jwt.JwtProvider;
import com.vizit.vizitapi.infra.kakao.KakaoClient;
import com.vizit.vizitapi.infra.kakao.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public TokenResponse kakaoLogin(KakaoLoginRequest request) {
        KakaoUserInfoResponse userInfo = kakaoClient.getUserInfo(request.getAccessToken());

        User user = userRepository.findByKakaoId(userInfo.getId())
                .orElseGet(() -> userRepository.save(User.create(
                        userInfo.getId(),
                        userInfo.getKakaoAccount().getProfile().getNickname(),
                        userInfo.getKakaoAccount().getProfile().getProfileImageUrl()
                )));

        return TokenResponse.of(jwtProvider.generate(user.getId()));
    }
}
