package com.vizit.vizitapi.infra.kakao;

import com.vizit.vizitapi.global.exception.CustomException;
import com.vizit.vizitapi.global.exception.ErrorCode;
import com.vizit.vizitapi.infra.kakao.dto.KakaoUserInfoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class KakaoClient {

    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestClient restClient = RestClient.create();

    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        try {
            KakaoUserInfoResponse response = restClient.get()
                    .uri(USER_INFO_URL)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(KakaoUserInfoResponse.class);
            if (response == null) {
                throw new CustomException(ErrorCode.AUTH_KAKAO_API_FAILED);
            }
            return response;
        } catch (HttpClientErrorException e) {
            throw new CustomException(ErrorCode.AUTH_INVALID_TOKEN, e);
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.AUTH_KAKAO_API_FAILED, e);
        }
    }
}
