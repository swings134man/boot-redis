package com.boot.redis.seconds.business;

import com.boot.redis.seconds.domain.Tokens;
import org.springframework.data.repository.CrudRepository;


public interface SeRepository extends CrudRepository<Tokens, String> {
}
