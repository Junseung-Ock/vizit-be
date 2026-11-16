package com.vizit.vizitapi.global.security;

import com.vizit.vizitapi.domain.user.repository.UserRepository;
import com.vizit.vizitapi.global.exception.CustomException;
import com.vizit.vizitapi.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return new CustomUserDetails(userId);
    }
}
