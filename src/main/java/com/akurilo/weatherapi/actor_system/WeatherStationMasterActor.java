package com.akurilo.weatherapi.actor_system;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import dto.CenterDto;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;
import static com.akurilo.weatherapi.WeatherApiApplication.TIMEOUT_GET_MESSAGE;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
                .build();
    }

    private void sendCenterDto(CenterDto centerDto) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = ask(selection, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
        pipe(future, getContext().dispatcher()).to(sender());
    }
}
