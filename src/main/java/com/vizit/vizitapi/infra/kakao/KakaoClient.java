package com.vizit.vizitapi.infra.kakao;

import com.vizit.vizitapi.global.exception.CustomException;
import com.vizit.vizitapi.global.exception.ErrorCode;
import com.vizit.vizitapi.infra.kakao.dto.KakaoUserInfoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestClient restClient = RestClient.create();

    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        try {
            return restClient.get()
                    .uri(USER_INFO_URL)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(KakaoUserInfoResponse.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AUTH_KAKAO_API_FAILED);
        }
    }
}
