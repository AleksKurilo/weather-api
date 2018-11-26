package com.akurilo.weatherapi.actor_system;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import dto.CenterDto;
import dto.LocationDto;
import dto.UserDto;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;
import static com.akurilo.weatherapi.WeatherApiApplication.TIMEOUT_GET_MESSAGE;

import java.util.concurrent.CompletableFuture;

public class WeatherStationMasterActor extends AbstractActor {

    private ActorSelection selection;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        selection =
                getContext().actorSelection("akka://ClusterSystem@127.0.0.1:2552/user/masterActor");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CenterDto.class, this::sendCenterDto)
                .match(UserDto.class, this::sendUserDto)
                .match(LocationDto.class, this::sendLocationDto)
                .build();
    }

    private void sendCenterDto(CenterDto centerDto) {
        CompletableFuture<Object> future = ask(selection, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        pipe(future, getContext().dispatcher()).to(sender());
    }

    private void sendUserDto(UserDto userDto){
        CompletableFuture<Object> future = ask(selection, userDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        pipe(future, getContext().dispatcher()).to(sender());
    }

    private void sendLocationDto(LocationDto locationDto){
        CompletableFuture<Object> future = ask(selection, locationDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        pipe(future, getContext().dispatcher()).to(sender());
    }
}
