package com.boot.redis.seconds.business;

import com.boot.redis.seconds.domain.Tokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/************
 * @info : Redis - Token Test Service
 * @name : SeService
 * @date : 2023/03/30 2:10 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Service
@RequiredArgsConstructor
@Slf4j
public class SeService {

    private final SeRepository repository; // RedisRepository
    private final SeDAO dao; //RedisTemplate

//    private final

    /**
     * @info    : 토큰 저장 - 간단.(RedisRepository)
     * @name    : saveToken
     * @date    : 2023/03/30 2:13 AM
     * @author  : SeokJun Kang(swings134@gmail.com)
     * @version : 1.0.0
     * @return
     */
    @Transactional
    public Tokens saveToken(Tokens tokens) {
        Tokens save = repository.save(tokens);
        log.info("Service save Log = {}", save);

        return save;
    }//save

    // Token 조회 - 간단(RedisRepository)
    @Transactional(readOnly = true)
    public Tokens getToken(String id) {
        Optional<Tokens> byId = repository.findById(id);

        if(byId.isPresent()){
            return byId.get();
        }else {
            throw new RuntimeException("No DATA");
        }
    }// 조회


    // RedisTemplate - Token save
    public void templatesSave(Tokens tokens) {
        dao.saveToken(tokens);
    }

}
