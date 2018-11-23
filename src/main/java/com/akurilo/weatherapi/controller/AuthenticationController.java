package com.akurilo.weatherapi.controller;

import com.akurilo.weatherapi.security.JWTUtil;
import com.akurilo.weatherapi.security.PBKDF2Encoder;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JWTUtil jwtUtil;

    private final PBKDF2Encoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @PostMapping(path = "login")
    public Mono<ResponseEntity<AuthResponseDto>> auth(@RequestBody AuthRequestDto ar) {
        return Mono.just(userDetailsService.loadUserByUsername(ar.getEmail()))
                .map(userDetails -> {
                    if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
                        return ResponseEntity.ok(new AuthResponseDto(jwtUtil.generateToken(userDetails)));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                });
    }


}
