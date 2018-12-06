package com.akurilo.weatherapi.security.config;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.akurilo.weatherapi.actor_system.WeatherStationMasterActor;
import com.akurilo.weatherapi.security.PBKDF2Encoder;
import dto.AuthRequestDto;
import dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static com.akurilo.weatherapi.WeatherApiApplication.ACTOR_SYSTEM;
import static com.akurilo.weatherapi.WeatherApiApplication.TIMEOUT_GET_MESSAGE;

@Component
@RequiredArgsConstructor
public class UserAuthorizationServiceImpl implements UserAuthorizationService {

    private final PBKDF2Encoder passwordEncoder;

    private static Mono<UserDetails> apply(UserDto dto) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(dto.getRole()));
        return Mono.just(new User(dto.getEmail(), dto.getPassword(), grantedAuthorities));
    }

    @Override
    public Mono<UserDetails> getUserByEmail(AuthRequestDto authRequestDto) throws UsernameNotFoundException {

        ActorRef weatherStationMasterActor = ACTOR_SYSTEM.actorOf(Props.create(WeatherStationMasterActor.class));
        CompletableFuture<Object> message = ask(weatherStationMasterActor, authRequestDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();

        return Mono
                .fromFuture(message)
                .cast(UserDto.class)
                .flatMap(UserAuthorizationServiceImpl::apply)
                .doOnError(er -> {
                    throw new UsernameNotFoundException("ERROR UsernameNotFoundException:", er);
                });
    }
}
