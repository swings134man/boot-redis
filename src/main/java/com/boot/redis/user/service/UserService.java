package com.boot.redis.user.service;

import com.boot.redis.user.domain.User;
import com.boot.redis.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/************
 * @info : User 관련
 * @name : UserService
 * @date : 2023/03/30 7:01 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService  {

    private final UserJpaRepository repository;

    // name으로 member 조회
    public User findByWriter(String name) {
        User result = repository.findByName(name);
        return result;
    }

}
