package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.Board;
import com.boot.redis.business.board.domain.BoardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public BoardDTO saveBoard(BoardDTO inDTO) {
        Board board = inDTO.toEntity(inDTO); //Entity

        Board save = repository.save(board);

        // DTO
        BoardDTO out = modelMapper.map(save, BoardDTO.class);

        return out;
    }

    // Update
    @Transactional
    @CachePut(value = "BoardDTO", key = "#inDTO.id", cacheManager = "cacheManager", unless = "#id == ''")
    public BoardDTO updateBoard(BoardDTO inDTO) {
        Board entity = repository.findById(inDTO.getId()).orElseThrow(() -> new IllegalArgumentException("해당 Post가 존재하지 않음."));
        entity.setId(inDTO.getId());
        entity.setTitle(inDTO.getTitle());
        entity.setContent(inDTO.getContent());
        entity.setWriter(inDTO.getWriter());
        repository.save(entity);

        BoardDTO result = modelMapper.map(entity, BoardDTO.class);
        return result;
    }

    // findAll
    @Transactional(readOnly = true)
    public List<BoardDTO> findAllPost() {
        List<Board> result = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));//ID 기준 DESC Sort FindALL

        // Entity To DTO
        List<BoardDTO> out = result.stream()
                .map(data -> modelMapper.map(data, BoardDTO.class))
                .collect(Collectors.toList());

        return out;
    }

    // findById - Cache
    // unless = id 가 '' 일때는 캐싱하지 않음.
    @Cacheable(value = "BoardDTO", key = "#id", cacheManager = "cacheManager", unless = "#id == ''")
    @Transactional(readOnly = true)
    public BoardDTO findByPostId(Long id) {
        Optional<Board> byId = repository.findById(id);
        if(byId.isPresent()){
            BoardDTO out = modelMapper.map(byId.get(), BoardDTO.class);
            log.info("Service Log {}", out);
            return out;
        }else {
            throw new IllegalStateException("해당 Post 가 존재하지 않음.");
        }
    }// findById

    // delete
    @CacheEvict(value = "BoardDTO", key = "#id", cacheManager = "cacheManager")
    @Transactional
    public boolean deleteBoard(Long id) {
        Board board = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 Post 존재하지 않음"));
        repository.delete(board);
        return true;
    }
}
