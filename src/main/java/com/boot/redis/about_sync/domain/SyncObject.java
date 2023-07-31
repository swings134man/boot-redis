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

    private int value;
    private String name;
}
