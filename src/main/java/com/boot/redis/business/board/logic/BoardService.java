package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.Board;
import com.boot.redis.business.board.domain.BoardDTO;
import com.boot.redis.config.annotation.CheckConn;
import com.boot.redis.user.domain.User;
import com.boot.redis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    private final UserService userService;
    private final BoardJpaReposittory repository;
    private final ModelMapper modelMapper;
    private final RedisTemplate redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final String FIRST_COMED = "first_comed";
    private final String FIRST_COMED_LIST = "first_comed_list";


    // save
    @Transactional
    public BoardDTO saveBoard(BoardDTO inDTO) {
        User userEntity = userService.findByWriter(inDTO.getWriter());

        Board board = inDTO.toEntity(inDTO); //Entity
        board.setUser(userEntity);

        Board save = repository.save(board);

        // DTO
        BoardDTO out = modelMapper.map(save, BoardDTO.class);

        return out;
    }

    // Update
    @Transactional
    @CheckConn
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
    @CheckConn
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

    /**
     * Async Test 메서드
     */
    @Async("asyncThread")
    public CompletableFuture<String> asyncInsertTest(BoardDTO inDTO){

        log.info("Async Thread = {}", Thread.currentThread().getName()); //async-thread-1

        CompletableFuture<String> res = CompletableFuture.supplyAsync(() -> {
            String out = "OK";
            log.info("Async Thread = {}", Thread.currentThread().getName()); // ForkJoinPool.commonPool-worker-1
            try {
                Thread.sleep(10000);
                User userEntity = userService.findByWriter(inDTO.getWriter());
                Board board = inDTO.toEntity(inDTO); //Entity
                board.setUser(userEntity);
                repository.save(board);
                return out;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return res;
    }

    public List<Board> findByBoardWithUser(Long id) {
        List<Board> byBoardWithUser = repository.findBoardByIdWithUser(id);
        log.info("Board With User : {}", byBoardWithUser);
        return byBoardWithUser;
    }

    // Redis 선착순 적재
    public boolean eventLimitCnt(Long userId) {

        if(!redisTemplate.hasKey(FIRST_COMED)){
            // incr Key = String Value(In Redis) -> Redis 내부적으로 정수변환 후 증가 다시 문자열로 저장.
            redisTemplate.opsForValue().set(FIRST_COMED, 0);
        }

        long count = Long.parseLong(stringRedisTemplate.opsForValue().get(FIRST_COMED));


        if(count < 100) {
            // Redis Count Increase
            stringRedisTemplate.opsForValue().increment(FIRST_COMED);
            log.info("Redis Value : {}", redisTemplate.opsForValue().get(FIRST_COMED));

            // Redis List Add
            stringRedisTemplate.opsForList().rightPush(FIRST_COMED_LIST, userId.toString());
        }else {
            return false;
        }
        return true;
    }

    // Excel Download Retrieve
    public Page<Board> findAllByOrderByCreatedDateDesc(Pageable pageable) {
        Page<Board> result = repository.findAllByOrderByCreatedDateDesc(pageable);
        log.info("Pageable Result : {}", result.getContent().size());
        return result;
    }


}
