package com.boot.redis.business.board.domain;

import com.boot.redis.config.BaseTimeEntity;
import com.boot.redis.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/************
 * @info : Board - 게시판 Entity
 * @name : Board
 * @date : 2023/04/01 5:41 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Entity
@Getter
@Setter
@Table(name = "board")
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title; //제목
    @Column(columnDefinition = "TEXT")
    private String content; // 내용
    private String writer; //작성자 ID


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
}
