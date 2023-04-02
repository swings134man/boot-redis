package com.boot.redis.user.repository;


import com.boot.redis.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where u.name = :name")
    User findByName(@Param("name") String name);
}
