package com.boot.redis.seconds.business;

import com.boot.redis.seconds.domain.Tokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/************
 * @info : Redis - Token Test Controller
 * @name : SeController
 * @date : 2023/03/30 2:10 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@RestController
@RequiredArgsConstructor
@Slf4j
public class SeController {

    private final SeService service;

    // RedisRepository - 저장
    @PostMapping("/redis/token/v1/save")
    public Tokens saveToken(@RequestBody Tokens tokens) {
        log.info("Controller Request Body = {}", tokens);

        Tokens res = service.saveToken(tokens);
        return res;
    }
    // RedisRepository - 조회
    @GetMapping("/redis/token/v1/get")
    public Tokens getToken(@RequestParam String id) {
        log.info("Controller Request Param = {}", id);

        Tokens token = service.getToken(id);
        return token;
    }

    //RedisTemplate - save(Tokens)
    @PostMapping("/redis/token/v1/template/save")
    public void templateSave(@RequestBody Tokens tokens) {
        service.templatesSave(tokens);
    }


}//class
