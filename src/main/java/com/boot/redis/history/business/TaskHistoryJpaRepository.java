package com.boot.redis.history.business;

import com.boot.redis.history.domain.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryJpaRepository extends JpaRepository<TaskHistory, Long> {
}
