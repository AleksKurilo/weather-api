package com.akurilo.weatherapi.controller;

import dto.UserDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static enums.RequestType.*;

@RestController
@RequestMapping(path = "/user")
public class UserController extends BaseController<UserDto> {

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<UserDto> create(@RequestBody UserDto userDto) {
        userDto.setRequestType(POST);
        return getDtoMono(userDto);
    }

    @PutMapping(path = "{id}/update", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<UserDto> update(@RequestBody UserDto userDto, @PathVariable Long id) {
        userDto.setId(id);
        userDto.setRequestType(PUT);
        return getDtoMono(userDto);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<UserDto> getById(@PathVariable long id) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setRequestType(GET);
        return getDtoMono(userDto);
    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<UserDto> getList() {
        UserDto userDto = new UserDto();
        userDto.setRequestType(GET_LIST);
        return getDtoFlux(userDto);
    }

    @DeleteMapping(path = "{id}/delete")
    public Mono<UserDto> deleteById(@PathVariable long id){
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setRequestType(DELETE);
        return getDtoMono(userDto);
    }
}
