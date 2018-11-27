package com.akurilo.weatherapi.controller;

import com.akurilo.weatherapi.security.JWTUtil;
import com.akurilo.weatherapi.security.PBKDF2Encoder;
import com.akurilo.weatherapi.security.config.UserAuthorizationService;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JWTUtil jwtUtil;

    private final PBKDF2Encoder passwordEncoder;

    private final UserAuthorizationService userAuthorizationService;

    @PostMapping(path = "login")
    public Mono<ResponseEntity<AuthResponseDto>> login(@RequestBody AuthRequestDto authRequestDto) {
        return userAuthorizationService.getUserByEmail(authRequestDto)
                .map(userDetails -> {
                    if (passwordEncoder.encode(authRequestDto.getPassword()).equals(userDetails.getPassword())) {
                        return ResponseEntity.ok(new AuthResponseDto(jwtUtil.generateToken(userDetails)));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                });
    }


}
