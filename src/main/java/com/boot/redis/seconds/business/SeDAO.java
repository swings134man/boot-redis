package com.boot.redis.seconds.business;


import com.boot.redis.seconds.domain.Tokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

/************
 * @info : Redis - DAO : RedisTemplate 사용 클래스
 * @name : SeDAO
 * @date : 2023/03/30 2:12 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : 모든 토큰 관리(testToken)을 set 타입으로 저장 -> hash(유저)에 대한정보들을 담는다.
 *
 *  -> set - userId
 *  ---> userId(Hash) -> AT,RT
 *
 ************/
@Repository
@RequiredArgsConstructor
@Slf4j
public class SeDAO {

    private final RedisTemplate<String, String> template;

    // SET - testToken:{userId}의 형태로 유저:토큰(AT,RT)을 관리한다.
    public void testToken(String userId) {
        SetOperations<String, String> setOperations = template.opsForSet();
        setOperations.add("testToken", userId);

        log.info("SET 저장 완료 = {}", setOperations.members("testToken")); //UserId가 set에 저장되었는지 확인.
    }

    // Token 저장 RedisTemplate - Hash
    public void saveToken(Tokens tokens) {
        // SET 지정 - Set에 해당 유저를 관리하도록 지정함.(smembers)
        testToken(tokens.getId());

        // Hash - testToken 하위 = testToken:{userId}
        HashOperations<String, Object, Object> hashOperations = template.opsForHash();
        String key = tokens.getId();

        hashOperations.put(key, "userId", tokens.getId());
        hashOperations.put(key, "accessToken", tokens.getAccessToken());
        hashOperations.put(key, "refreshToken", tokens.getRefreshToken());

        log.info("Hash save userId = {} ", hashOperations.get(key, "userId"));
        log.info("Hash save AT = {} ", hashOperations.get(key, "accessToken"));
        log.info("Hash save RT = {} ", hashOperations.get(key, "refreshToken"));
    }

}
