package com.akurilo.weatherapi.controller;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.akurilo.weatherapi.actor_system.WeatherStationMasterActor;
import dto.CenterDto;
import enums.RequestType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static com.akurilo.weatherapi.WeatherApiApplication.ACTOR_SYSTEM;
import static com.akurilo.weatherapi.WeatherApiApplication.TIMEOUT_GET_MESSAGE;


@RestController
@RequestMapping(path = "/centre")
public class CenterController {

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<CenterDto> create(@RequestBody CenterDto centerDto) {
        centerDto.setRequestType(RequestType.POST);
        return getCenterDtoMono(centerDto);
    }

    @PutMapping(path = "{id}/update", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<CenterDto> update(@RequestBody CenterDto centerDto, @PathVariable Long id) {
        centerDto.setId(id);
        centerDto.setRequestType(RequestType.PUT);
        return getCenterDtoMono(centerDto);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<CenterDto> getById(@PathVariable long id) {
        CenterDto centerDto = new CenterDto();
        centerDto.setId(id);
        centerDto.setRequestType(RequestType.GET);

        return getCenterDtoMono(centerDto);

    }

    private Mono<CenterDto> getCenterDtoMono(CenterDto centerDto) {
        ActorRef weatherStationMasterActor = ACTOR_SYSTEM.actorOf(Props.create(WeatherStationMasterActor.class));
        CompletableFuture<Object> message = ask(weatherStationMasterActor, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        return Mono
                .fromFuture(message)
                .flatMapMany(it -> Flux.fromIterable((List<CenterDto>) it))
                .single()
                .doOnError(er -> {
                    throw new RuntimeException("ERROR:", er);
                });
    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<CenterDto> getList() {
        CenterDto centerDto = new CenterDto();
        centerDto.setRequestType(RequestType.GET_LIST);

        ActorRef weatherStationMasterActor = ACTOR_SYSTEM.actorOf(Props.create(WeatherStationMasterActor.class));
        CompletableFuture<Object> message = ask(weatherStationMasterActor, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        return Mono
                .fromFuture(message)
                .flatMapMany(it -> Flux.fromIterable((List<CenterDto>) it))
                .doOnError(er -> {
                    throw new RuntimeException("ERROR:", er);
                });
    }
}
