package com.akurilo.weatherapi.controller;

import dto.CenterDto;
import dto.LocationDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static enums.RequestType.*;


@RestController
@RequestMapping("/location")
public class LocationController extends BaseController<LocationDto> {

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<LocationDto> create(@RequestBody LocationDto locationDto) {
        locationDto.setRequestType(POST);
        return getDtoMono(locationDto);
    }

    @PutMapping(path = "{id}/update", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<LocationDto> update(@RequestBody LocationDto locationDto, @PathVariable Long id) {
        locationDto.setId(id);
        locationDto.setRequestType(PUT);
        return getDtoMono(locationDto);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<LocationDto> getById(@PathVariable long id) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(id);
        locationDto.setRequestType(GET);
        return getDtoMono(locationDto);

    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<LocationDto> getList() {
        LocationDto locationDto = new LocationDto();
        locationDto.setRequestType(GET_LIST);
        return getDtoFlux(locationDto);
    }

    @DeleteMapping(path = "{id}/delete")
    public Mono<LocationDto> deleteById(@PathVariable long id){
        LocationDto locationDto = new LocationDto();
        locationDto.setId(id);
        locationDto.setRequestType(DELETE);
        return getDtoMono(locationDto);
    }
}
