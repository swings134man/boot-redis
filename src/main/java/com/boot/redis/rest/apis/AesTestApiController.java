package com.boot.redis.rest.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// For AES encryption test
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aes")
public class AesTestApiController {

    private final AesTestApiService service;

    @GetMapping("/encrypt")
    public String encryptString(@RequestParam(required = true) String str) throws Exception {
        return service.encryptString(str);
    }

    @GetMapping("/decrypt")
    public String decryptString(@RequestParam(required = true) String str) throws Exception {
        return service.decryptString(str);
    }
}
