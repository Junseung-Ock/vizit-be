package com.vizit.vizitapi.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class KakaoLoginRequest {

    @NotBlank(message = "카카오 액세스 토큰은 필수입니다.")
    private String accessToken;
}
