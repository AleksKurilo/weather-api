package com.akurilo.weatherapi.controller;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.akurilo.weatherapi.actor_system.WeatherStationMasterActor;
import dto.BaseEntityDto;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static com.akurilo.weatherapi.WeatherApiApplication.ACTOR_SYSTEM;
import static com.akurilo.weatherapi.WeatherApiApplication.TIMEOUT_GET_MESSAGE;


public abstract class BaseController <T extends BaseEntityDto> {

    protected Mono<T> getDtoMono(T dto) {
        ActorRef weatherStationMasterActor = ACTOR_SYSTEM.actorOf(Props.create(WeatherStationMasterActor.class));
        CompletableFuture<Object> message = ask(weatherStationMasterActor, dto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        return Mono
                .fromFuture(message)
                .flatMapMany(m -> Flux.fromIterable((List<T>) m))
                .single()
                .doOnError(er -> {
                    throw new RuntimeException("ERROR:", er);
                });
    }

    protected Flux<T> getDtoFlux(T dto){
        ActorRef weatherStationMasterActor = ACTOR_SYSTEM.actorOf(Props.create(WeatherStationMasterActor.class));
        CompletableFuture<Object> message = ask(weatherStationMasterActor, dto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        return Mono
                .fromFuture(message)
                .flatMapMany(it -> Flux.fromIterable((List<T>) it))
                .doOnError(er -> {
                    throw new RuntimeException("ERROR:", er);
                });
    }
}
