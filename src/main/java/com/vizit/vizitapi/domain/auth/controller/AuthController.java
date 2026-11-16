package com.vizit.vizitapi.domain.auth.controller;

import com.vizit.vizitapi.domain.auth.dto.KakaoLoginRequest;
import com.vizit.vizitapi.domain.auth.dto.TokenResponse;
import com.vizit.vizitapi.domain.auth.service.AuthService;
import com.vizit.vizitapi.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoLogin(@RequestBody @Valid KakaoLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.kakaoLogin(request)));
    }
}
