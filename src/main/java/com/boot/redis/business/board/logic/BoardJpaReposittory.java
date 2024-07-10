package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardJpaReposittory extends JpaRepository<Board, Long> {
    Page<Board> findAllByOrderByCreatedDateDesc(Pageable pageable);


    @Query("select b from Board b JOIN FETCH b.user where b.id = :id")
    @EntityGraph(attributePaths = {"user"})
    List<Board> findBoardByIdWithUser(@Param("id") Long id);

}
