package com.boot.redis.history.business;

import com.boot.redis.config.common.TaskOperationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    @PostMapping("/task/save")
    public CompletableFuture<ResponseEntity<?>> save(@RequestParam String taskName) throws InterruptedException {
        return taskHistoryService.save(taskName)
                .thenApply(result -> {
                    if (result.isSuccess()) {
                        log.warn("3. TaskHistory 저장 완료.");
                        return ResponseEntity.ok(result);
                    } else {
                        return new ResponseEntity<>(result.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .exceptionally(ex -> {
                    log.error("TaskHistory 저장 중 에러 발생", ex);
                    return new ResponseEntity<>("TaskHistory 저장 중 에러 발생: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }
}
