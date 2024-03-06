package com.boot.redis.open.controller;

import com.boot.redis.config.util.RandomStringGen;
import com.boot.redis.open.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/open")
public class OpenApiController {

    private final OpenApiService openApiService;

    @RequestMapping(value = "/v1/test", produces = "application/json", method = {RequestMethod.GET})
    public String test(@RequestParam(required = false, name = "name") String name) {
        log.info("Request Name : {}", name);
        return name;
    }

    @RequestMapping(value = "/v1/keyGen", produces = "application/json", method = {RequestMethod.GET})
    public String keyGen() {
        return openApiService.genKey();
    }

}
