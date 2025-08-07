package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.s3.S3Uploader;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SimpleController {

    private final S3Uploader s3Uploader;

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    /**
     * S3에 이미지 업로드
     * @param image 업로드할 이미지 파일
     * @return 업로드된 이미지의 URL과 함께 성공 메시지
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        // S3Uploader 서비스를 사용하여 파일을 'images' 디렉토리 아래에 업로드
        String imageUrl = s3Uploader.upload(image, "images");

        // 업로드된 이미지의 URL을 응답으로 반환
        return ResponseEntity.ok("이미지 URL: " + imageUrl);
    }
}
