package com.akurilo.weatherapi.controller;

import dto.StationDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static enums.RequestType.*;


@RestController
@RequestMapping("/station")
public class StationController extends BaseController<StationDto> {

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<StationDto> create(@RequestBody StationDto stationDto) {
        stationDto.setRequestType(POST);
        return getDtoMono(stationDto);
    }

    @PutMapping(path = "{id}/update", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<StationDto> update(@RequestBody StationDto stationDto, @PathVariable Long id) {
        stationDto.setId(id);
        stationDto.setRequestType(PUT);
        return getDtoMono(stationDto);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<StationDto> getById(@PathVariable long id) {
        StationDto stationDto = new StationDto();
        stationDto.setId(id);
        stationDto.setRequestType(GET);
        return getDtoMono(stationDto);

    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<StationDto> getList() {
        StationDto stationDto = new StationDto();
        stationDto.setRequestType(GET_LIST);
        return getDtoFlux(stationDto);
    }

    @DeleteMapping(path = "{id}/delete")
    public Mono<StationDto> deleteById(@PathVariable long id) {
        StationDto stationDto = new StationDto();
        stationDto.setId(id);
        stationDto.setRequestType(DELETE);
        return getDtoMono(stationDto);
    }
}
