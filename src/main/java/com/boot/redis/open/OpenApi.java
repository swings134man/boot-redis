package com.boot.redis.open;

import com.boot.redis.config.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "API_MNGR")
public class OpenApi extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "API_MNGR_ID")
    private Long apiMngrId;

    @Column(name = "API_KEY", nullable = false)
    private String apiKey;

    @Column(name = "API_MNGR_NAME")
    private String apiMngrName;

    @Column(name = "API_MNGR_STATUS")
    private String apiMngrStatus;

    @Column(name = "API_MNGR_EXPR_DATE")
    private String apiMngrExprDate;


}
