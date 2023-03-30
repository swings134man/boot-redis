package com.boot.redis.first.repository;

import com.boot.redis.first.domain.RedisUser;
import org.springframework.data.repository.CrudRepository;

/************
 * @info : Redis User Repository
 * @name : UserRedisRepositroy
 * @date : 2023/03/29 2:52 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : Spring Data JPA 와 같은 방법.
 ************/
public interface UserRedisRepositroy extends CrudRepository<RedisUser, String> {
}
