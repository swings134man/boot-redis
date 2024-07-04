package com.boot.redis.config.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class TaskOperationResult {

    @Getter
    private boolean success;
    private String message;
    private Object data;

    public TaskOperationResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
