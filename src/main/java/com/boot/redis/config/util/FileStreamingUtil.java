package com.boot.redis.config.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @package : com.boot.redis.config.util
 * @name : FileStreamingUtil.java
 * @date : 2025. 5. 4. 오후 7:44
 * @author : lucaskang(swings134man)
 * @Description: File Download Streaming Util Class
 * - 사용자에게 File 을 Streaming 방식으로 전송 하는 Util Class
**/
@Component
@Slf4j
public class FileStreamingUtil {

    private static final int BUFFER_SIZE = 8192; // 8KB : 기본값(필요시 조절)
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String CONTENT_TYPE = "application/octet-stream";

    /**
    * @methodName : downloadByStreaming
    * @date : 2025. 5. 4. 오후 8:00
    * @author : lucaskang
    * @param filePath : 다운로드할 파일의 경로
    * @param httpResponse : HTTP 응답 객체
    * @Description: File 객체를 사용하지 않고 NIO 객체 Path, Files 사용
     * 속도에서의 차이는 거의 없음, 다만 안정성 면에서 NIO 가 더 좋음
     * 또한, OS 별 최적화가 되어있음(내부적으로 Native Code 사용)
     * File 은 실제 물리파일을 추적 및 조작
     * Files 는 Symlink 기반, 조작도 가능, Files.delete() => symlink 면 symlink 삭제 아니면 물리파일 삭제함
     * 또한 Files 는 메타데이터 다룸(소유자, 권한, 링크)
    **/
    public void downloadByStreaming(String filePath, HttpServletResponse httpResponse) throws IOException {
        // filePath 에 파일명까지 있다고 가정
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            log.error("File not found: {}", filePath);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + filePath);
        }

        httpResponse.setContentType(CONTENT_TYPE);
        httpResponse.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\""); // 공백, 특수문자 방지
        httpResponse.setContentLengthLong(Files.size(path));

        // try-with-resources 로 자원 안전하게 닫기
        try (InputStream in = Files.newInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(in);
             BufferedOutputStream bos = new BufferedOutputStream(httpResponse.getOutputStream())) {

            byte[] buffer = new byte[BUFFER_SIZE];

            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.flush(); // 혹시 남은 데이터 강제로 밀어내기
        } catch (IOException e) {
            log.error("Error during file streaming: {}", e.getMessage(), e);
            throw e;
        }
    }

}//class
