package com.vizit.vizitapi.infra.kakao;

import com.vizit.vizitapi.global.exception.CustomException;
import com.vizit.vizitapi.global.exception.ErrorCode;
import com.vizit.vizitapi.infra.kakao.dto.KakaoUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Component
public class KakaoClient {

    private static final String USER_INFO_PATH = "/v2/user/me";

    private final RestClient restClient;

    public KakaoClient(
            RestClient.Builder builder,
            @Value("${kakao.api.base-url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));
        this.restClient = builder
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        try {
            KakaoUserInfoResponse response = restClient.get()
                    .uri(USER_INFO_PATH)
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
