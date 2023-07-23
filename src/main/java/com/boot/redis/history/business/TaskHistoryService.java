package com.boot.redis.history.business;

import com.boot.redis.history.domain.TaskHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskHistoryService {

    private final TaskHistoryJpaRepository repository;

    public void save(String taskName, String taskType, boolean taskStatus, String taskAdminId) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTaskName(taskName);
        taskHistory.setTaskType(taskType);
        taskHistory.setTaskStatus(taskStatus);
        taskHistory.setTaskAdminId(taskAdminId);

        repository.save(taskHistory);
    }

    @Async
    public CompletableFuture<Object> save(String taskName) throws InterruptedException {

        // ~ Task ~
        log.warn("1. CRUD 작업 중 -> {}", Thread.currentThread().getName()); // async-thread-1
        Thread.sleep(3000);

        TaskHistory taskHistory = new TaskHistory();
        return CompletableFuture.supplyAsync(() -> {
            try{
                log.warn("2. Async 작업 중 -> {}", Thread.currentThread().getName()); // ForkJoinPool.commonPool-worker-1
                taskHistory.setTaskName(taskName);
                taskHistory.setTaskType("C");
                taskHistory.setTaskStatus(true);
                taskHistory.setTaskAdminId("admin");

                repository.save(taskHistory);
            }catch (RuntimeException e) {
                e.printStackTrace();
                taskHistory.setTaskName(taskName);
                taskHistory.setTaskType("C");
                taskHistory.setTaskStatus(false);
                taskHistory.setTaskAdminId("admin");

                repository.save(taskHistory);
                return null;
            }
            return null;
        });
    }
}
