package com.boot.redis.first.service;

import com.boot.redis.first.domain.RedisUser;
import com.boot.redis.first.repository.UserRedisRepositroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
public class FirstUserSerivce {

    private final UserRedisRepositroy repository;
    private final RedisTemplate redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final String OTP_PREFIX = "OTP:";

    /**
     * @info    : Redis - save (User)
     * @name    : addUser
     * @date    : 2023/03/29 5:45 PM
     * @author  : SeokJun Kang(swings134@gmail.com)
     * @version : 1.0.0
     * @Description :
     */
    @Transactional
    public RedisUser addUser(RedisUser redisUser) {
        // save
        RedisUser save = repository.save(redisUser);

        // find
        Optional<RedisUser> result = repository.findById(save.getId());

        // Handling
        if(result.isPresent()) {
            return result.get();
        }else {throw new RuntimeException("Database has no Data");}
    }//save

    /**
     * @info    : Redis - get
     * @name    : name
     * @date    : 2023/03/29 5:45 PM
     * @author  : SeokJun Kang(swings134@gmail.com)
     * @version : 1.0.0
     * @Description :
     * @return
     */
    @Transactional(readOnly = true)
    public RedisUser getUserById(String reqId) {
        Optional<RedisUser> result = repository.findById(reqId);

        // Handling
        if(result.isPresent()) {
            return result.get();
        }else {throw new RuntimeException("Database has no Data");}
    }

    // List 포함된 POst
    public RedisUser postForList(RedisUser redisUser) {
        RedisUser save = repository.save(redisUser);

        // find
        Optional<RedisUser> result = repository.findById(redisUser.getId());

        if(result.isPresent()){
            return result.get();
        }else {
            throw new RuntimeException("NO Data");
        }
    }


    /**
     * @info : 임시 비밀번호 요청 (OTP)
     * @param userId
     * @return
     */
    public String requestOtp(String userId) {
        stringRedisTemplate.opsForValue().set(OTP_PREFIX + userId, genOtpKey(), 3 * 60, TimeUnit.SECONDS);

        log.info("Temporay Password set : {}", redisTemplate.opsForValue().get(userId));

        return (String)redisTemplate.opsForValue().get(userId);
    }


    /**
     * @info : 임시 비밀번호 확인 (OTP)
     * @param id
     * @param otp
     * @return
     */
    public String checkOtp(String id, String otp) {

        String target = OTP_PREFIX + id;
        log.info("target : {}", target);

        if(redisTemplate.hasKey(OTP_PREFIX + id)){
            String value = (String)redisTemplate.opsForValue().get(target);

            log.info("value : {}", value);


            if(value.equals(otp)) {
                log.info("OTP is Correct");
                return "SUCCESS";
            }else {
                return "FAIL";
            }
        }else {
            return "NO DATA";
        }
    }



    // 임시 비밀번호 생성(OTP)
    private String genOtpKey() {
        return RandomStringUtils.randomAlphanumeric(10); // Eng(Upper, Lower) + Number
    }
}
