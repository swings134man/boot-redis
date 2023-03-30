package com.boot.redis.first.controller;

import com.boot.redis.first.domain.RedisUser;
import com.boot.redis.first.service.FirstUserSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/************
 * @info : Redis User Controller
 * @name : UserController
 * @date : 2023/03/29 2:55 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final FirstUserSerivce serivce;

    /**
     * @info    : Redis에 User 정보를 저장한다.
     * @name    : addUser
     * @date    : 2023/03/29 2:57 AM
     * @author  : SeokJun Kang(swings134@gmail.com)
     * @version : 1.0.0
     * @param   :
     * @return  :
     * @Description :
     */
    @PostMapping("/redis/v1/post")
    public RedisUser addUser(@RequestBody RedisUser redisUser) {
        log.info("Controller Request: {}", redisUser);

        RedisUser result = serivce.addUser(redisUser);
        log.info("Controller result: {}", result);

        return result;
    }// save


    /**
     * @info    : Redis에 ID 값으로 유저 정보를 가져온다.
     * @name    : getUser
     * @date    : 2023/03/29 5:47 PM
     * @author  : SeokJun Kang(swings134@gmail.com)
     * @version : 1.0.0
     * @Description :
     */
    @GetMapping("/redis/v1/getUser")
    public RedisUser getUser(@RequestParam String reqId) {
        RedisUser redisUserById = serivce.getUserById(reqId);
        return redisUserById;
    }

    // List For Post
    @PostMapping("/redis/v1/list/post")
    public RedisUser postForList(@RequestBody RedisUser redisUser) {
        RedisUser result = serivce.postForList(redisUser);
        return result;
    }

}
