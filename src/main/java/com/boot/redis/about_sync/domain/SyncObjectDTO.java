package com.boot.redis.about_sync.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncObjectDTO {

    private Long id;
    private int value;
    private String name;
}
