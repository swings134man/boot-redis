package com.boot.redis.history.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    @PostMapping("/task/save")
    public ResponseEntity<Object> save(@RequestParam String taskName) throws InterruptedException {
        taskHistoryService.save(taskName).thenAccept((result) -> {
            log.warn("3. TaskHistory 저장 완료.");
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
