package com.boot.redis.about_sync.model;

import com.boot.redis.about_sync.domain.SyncObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyncJpaRepository extends JpaRepository<SyncObject, Long> {

    Optional<SyncObject> findByName(String name);
}
