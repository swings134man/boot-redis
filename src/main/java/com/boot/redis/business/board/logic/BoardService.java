package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.Board;
import com.boot.redis.business.board.domain.BoardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/************
 * @info : Board Service
 * @name : BoardService
 * @date : 2023/04/01 5:46 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardJpaReposittory repository;
    private final ModelMapper modelMapper;

    // save
    public BoardDTO saveBoard(BoardDTO inDTO) {
        Board board = inDTO.toEntity(inDTO); //Entity

        Board save = repository.save(board);

        // DTO
        BoardDTO out = BoardDTO.builder()
                .id(save.getId())
                .title(save.getTitle())
                .content(save.getContent())
                .writer(save.getWriter())
                .build();

        return out;
    }

    // findAll
    public List<BoardDTO> findAllPost() {
        List<Board> result = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));//ID 기준 DESC Sort FindALL

        // Entity To DTO
        List<BoardDTO> out = result.stream()
                .map(data -> modelMapper.map(data, BoardDTO.class))
                .collect(Collectors.toList());

        return out;
    }

    // findById - Cache
    public BoardDTO findByPostId(Long id) {
        Optional<Board> byId = repository.findById(id);
        if(byId.isPresent()){
            BoardDTO out = modelMapper.map(byId.get(), BoardDTO.class);
            log.info("Service Log {}", out);
            return out;
        }else {
            throw new IllegalStateException("해당 Post 가 존재하지 않음.");
        }
    }
}
