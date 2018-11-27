package com.akurilo.weatherapi.security.config;

import dto.AuthRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserAuthorizationService {

    Mono<UserDetails> getUserByEmail(AuthRequestDto authRequestDto);
}
