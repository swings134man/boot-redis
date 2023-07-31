package com.boot.redis.about_sync.model;

import com.boot.redis.about_sync.domain.SyncObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncJpaRepository extends JpaRepository<SyncObject, Long> {

    SyncObject findByName(String name);
}
