package com.boot.redis.business.board.logic;

import com.boot.redis.business.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaReposittory extends JpaRepository<Board, Long> {
    Page<Board> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
