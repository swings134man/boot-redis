package com.boot.redis.about_sync.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="sync_object")
@Getter @Setter @ToString
public class SyncObject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int value; // Target Value (Test Only)
    private String name;

    public void decrease() {
        checkCount();
        this.value -= 1;
    }

    private void checkCount() {
        if(value < 1) {
            throw new IllegalStateException("Count is less than 1");
        }
    }

}
