package com.boot.redis.first.service;

import com.boot.redis.first.domain.User;
import com.boot.redis.first.repository.UserRedisRepositroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/************
 * @info : Redis - User Service
 * @name : UserSerivce
 * @date : 2023/03/29 2:53 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserSerivce {

    private final UserRedisRepositroy repository;


    public User addUser(User user) {
        // save
        User save = repository.save(user);

        // find
        Optional<User> result = repository.findById(save.getId());

        // Handling
        if(result.isPresent()) {
            return result.get();
        }else {throw new RuntimeException("Database has no Data");}
    }//save


}
