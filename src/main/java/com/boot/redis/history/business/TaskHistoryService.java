package com.boot.redis.history.business;

import com.boot.redis.config.common.TaskOperationResult;
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

    public void save(String taskName, String taskType, String taskStatus, String taskAdminId) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTaskName(taskName);
        taskHistory.setTaskType(taskType);
        taskHistory.setTaskStatus(taskStatus);
        taskHistory.setTaskAdminId(taskAdminId);

        repository.save(taskHistory);
    }

    @Async
    public CompletableFuture<TaskOperationResult> save(String taskName) throws InterruptedException {

        // ~ Task ~
        log.warn("1. CRUD 작업 중 -> {}", Thread.currentThread().getName()); // async-thread-1

        TaskHistory taskHistory = new TaskHistory();
        CompletableFuture<TaskOperationResult> futureResult = CompletableFuture.supplyAsync(() -> {
            try {
                log.warn("2. Async 작업 중 -> {}", Thread.currentThread().getName()); // ForkJoinPool.commonPool-worker-1
                // ~ Task ~ (비즈니스 로직) 작업
                taskHistory.setTaskName(taskName);
                taskHistory.setTaskType("C");
                taskHistory.setTaskStatus("Y");
                taskHistory.setTaskAdminId("admin");

                repository.save(taskHistory);

                return new TaskOperationResult(true, "Success", taskHistory);
            } catch (RuntimeException e) {
                // 작업중 에러 발생시
                e.printStackTrace();
                taskHistory.setTaskName(taskName);
                taskHistory.setTaskType("C");
                taskHistory.setTaskStatus("N");
                taskHistory.setTaskAdminId("admin");

                repository.save(taskHistory);

                return new TaskOperationResult(false, e.getMessage(), null);
            }
        });

        return futureResult;
    }
}
