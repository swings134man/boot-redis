package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.BoardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/************
 * @info : Board Controller
 * @name : BoardController
 * @date : 2023/04/01 5:46 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService service;

    // save
    @PostMapping("/board/v1/save")
    public BoardDTO saveBoard(@RequestBody BoardDTO dto) {
        BoardDTO boardDTO = service.saveBoard(dto);
        return boardDTO;
    }

    // findAll
    @GetMapping("/board/v1/findAll")
    public List<BoardDTO> findAllPost() {
        List<BoardDTO> allPost = service.findAllPost();
        return allPost;
    }

    // findById
    @GetMapping("/board/v1/findById")
    public BoardDTO findByBoardId(@RequestParam Long id) {
        log.info("Requset Id {} ", id);
        BoardDTO byPostId = service.findByPostId(id);
        return byPostId;
    }

}//class
