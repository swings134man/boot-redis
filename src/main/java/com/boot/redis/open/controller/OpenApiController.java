package com.boot.redis.open.controller;

import com.boot.redis.open.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class OpenApiController {

    private final OpenApiService openApiService;

    @RequestMapping(value = "open/v1/test", produces = "application/json", method = {RequestMethod.GET})
    public String test(@RequestParam(required = false, name = "name") String name) {
        log.info("Request Name : {}", name);
        return name;
    }

    @RequestMapping(value = "open/v1/keyGen", produces = "application/json", method = {RequestMethod.GET})
    public String keyGen() {
        return openApiService.genKey();
    }

    @PutMapping(value = "/v1/reissueKey", produces = "application/json")
    public ResponseEntity<Object> reissueKey(Long id) {

        if(id == null || id <= 0) {
            log.warn("reissueKey - invalid ID");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        openApiService.reissueKey(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
