package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.Board;
import com.boot.redis.business.board.domain.BoardDTO;
import com.boot.redis.config.annotation.ActionMapping;
import com.boot.redis.config.util.ExcelUtil;
import com.boot.redis.config.util.constants.ExcelConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

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

    @GetMapping("/board/v1/withUser")
    public ResponseEntity<List<Board>> findByBoardWithUser(@RequestParam Long boardId){
        List<Board> byBoardWithUser = service.findByBoardWithUser(boardId);
        return new ResponseEntity<>(byBoardWithUser, HttpStatus.OK);
    }

    @GetMapping("/board/v1/firstComedIssue")
    public ResponseEntity<Boolean> firstComedIssue(@RequestParam Long userId) {
        boolean result = service.eventLimitCnt(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "board/v1/excel", produces = {"application/vnd.ms-excel"})
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) {

        // 엑셀 데이터를 해쉬맵 형태로 정의
        Map<String, Object> excelData = new HashMap<>();

        // 엑셀의 컬럼헤드 정의
        excelData.put(ExcelConstant.EX_HEADER_BG_COLOR, IndexedColors.YELLOW);
        LinkedHashMap<String, String> headerList = new LinkedHashMap<String, String>();
        headerList.put("id", "게시판ID");
        headerList.put("title", "제목");
        headerList.put("writer", "작성자");
        headerList.put("createdDate", "생성일");
        headerList.put("modifiedDate", "수정일");

        // 컬럼 헤드를 엑셀 데이터에 담기
        Map<String, Object> tempData = new HashMap<>();
        tempData.put(ExcelConstant.EX_HEADER_LIST, headerList);

        // FIXME:Need to DateFrom Change
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String fileName = "게시판_목록";
        tempData.put(ExcelConstant.EX_FILE_NAME, fileName);
        excelData.put(ExcelConstant.EX_EXCEL_DATA, tempData);

        ExcelUtil.downloadExcelToSxssf(excelData, request, response, (sheet) -> {
            AtomicInteger rowIdx = new AtomicInteger();

            int pageSize = 100;
            int pageNumber = 0;
            Page<Board> page;

            do {
                PageRequest pr = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
                page = service.findAllByOrderByCreatedDateDesc(pr);

                for (Board item : page.getContent()) {
                    // 데이터 입력
                    Row row = sheet.createRow(rowIdx.incrementAndGet());
                    int cellCnt = 0;
                    row.createCell(cellCnt++).setCellValue(item.getId());
                    row.createCell(cellCnt++).setCellValue(item.getTitle());
                    row.createCell(cellCnt++).setCellValue(item.getWriter());
                    row.createCell(cellCnt++).setCellValue(item.getContent());
                    row.createCell(cellCnt++).setCellValue(item.getCreatedDate().toString());
                    row.createCell(cellCnt++).setCellValue(item.getModifiedDate().toString());
            //          row.createCell(cellCnt++).setCellValue(formatSafely(LocalDateTime.parse(item.getCreatedDate()), formatter));
            //          row.createCell(cellCnt++).setCellValue(formatSafely(LocalDateTime.parse(item.getModifiedDate()), formatter));
                }

                pageNumber++;
            } while (page.hasNext());
        });
    }

    private String formatSafely(LocalDateTime dateTime, DateTimeFormatter timeFormatter) {
        log.debug("dateTime {}", dateTime);
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.format(timeFormatter))
                .orElse(null);
    }


}//class
