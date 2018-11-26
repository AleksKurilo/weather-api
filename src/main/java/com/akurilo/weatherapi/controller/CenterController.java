package com.akurilo.weatherapi.controller;

import dto.CenterDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static enums.RequestType.*;


@RestController
@RequestMapping("/center")
public class CenterController extends BaseController<CenterDto> {

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<CenterDto> create(@RequestBody CenterDto centerDto) {
        centerDto.setRequestType(POST);
        return getDtoMono(centerDto);
    }

    @PutMapping(path = "{id}/update", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<CenterDto> update(@RequestBody CenterDto centerDto, @PathVariable Long id) {
        centerDto.setId(id);
        centerDto.setRequestType(PUT);
        return getDtoMono(centerDto);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<CenterDto> getById(@PathVariable long id) {
        CenterDto centerDto = new CenterDto();
        centerDto.setId(id);
        centerDto.setRequestType(GET);
        return getDtoMono(centerDto);

    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<CenterDto> getList() {
        CenterDto centerDto = new CenterDto();
        centerDto.setRequestType(GET_LIST);
        return getDtoFlux(centerDto);
    }

    @DeleteMapping(path = "{id}/delete")
    public Mono<CenterDto> deleteById(@PathVariable long id){
        CenterDto centerDto = new CenterDto();
        centerDto.setId(id);
        centerDto.setRequestType(DELETE);
        return getDtoMono(centerDto);
    }
}
