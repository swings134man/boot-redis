package com.boot.redis.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserService implements UserDetailsService {

    // Security 에서 사용하기 위한 METHOD
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO : ID 혹은 Email로 조회 후 return.
        return null;
    }
}
