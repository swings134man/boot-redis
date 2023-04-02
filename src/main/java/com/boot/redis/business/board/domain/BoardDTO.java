package com.boot.redis.business.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private String writer;

    private String createdDate;
    private String modifiedDate;

    // to Entity
    public Board toEntity(BoardDTO in) {
        Board board = new Board();
        board.setTitle(in.title);
        board.setContent(in.content);
        board.setWriter(in.writer);
        return board;
    }


}
