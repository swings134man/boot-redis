package com.boot.redis.history.domain;

import com.boot.redis.config.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="task_history")
@Getter @Setter @ToString
public class TaskHistory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName; // 작업명
    private String taskType; // 작업타입(C,R,U,D)
    private boolean taskStatus; // 작업상태(true:성공, false:실패)
    private String taskAdminId; // 작업자 ID

}
