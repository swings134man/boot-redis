package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.BoardDTO;
import com.boot.redis.config.annotation.ActionMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @ActionMapping
    public BoardDTO saveBoard(@RequestBody BoardDTO dto) {
        BoardDTO boardDTO = service.saveBoard(dto);
        return boardDTO;
    }

    // update
    @PutMapping("/board/v1/update")
    public BoardDTO updateBoard(@RequestBody BoardDTO inDTO) {
        BoardDTO boardDTO = service.updateBoard(inDTO);
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
    @ActionMapping
    public BoardDTO findByBoardId(@RequestParam Long id) {
        log.info("Requset Id {} ", id);
        BoardDTO byPostId = service.findByPostId(id);
        return byPostId;
    }

    // delete
    @DeleteMapping("/board/v1/delete")
    public boolean deleteBoard(Long id) {
        boolean res = service.deleteBoard(id);
        return res;
    }

    @PostMapping("/board/v1/asyncTest")
    public ResponseEntity<CompletableFuture<String>> asyncTest(@RequestBody BoardDTO dto) throws InterruptedException {
        CompletableFuture<String> res = service.asyncInsertTest(dto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/board/v1/firstComedIssue")
    public ResponseEntity<Boolean> firstComedIssue(@RequestParam Long userId) {
        boolean result = service.eventLimitCnt(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}//class
