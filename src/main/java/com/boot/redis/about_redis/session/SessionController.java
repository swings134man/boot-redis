package com.boot.redis.about_redis.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/session")
public class SessionController {


    // Login 상황을 가정한 Session 저장
    @PostMapping
    public ResponseEntity<String> saveSession(HttpSession session, @RequestBody String userName) {
        session.setAttribute("userName", userName);
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    // Session 으로 Info Get
    @GetMapping
    public ResponseEntity<String> getSession(HttpSession session) {
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return new ResponseEntity<>("세션이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }
}
