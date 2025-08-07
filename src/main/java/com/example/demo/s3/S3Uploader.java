package com.example.demo.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    // S3와 통신하는 AWS 공식 클라이언트
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일을 업로드하고, 업로드된 파일의 URL을 반환합니다.
     * @param multipartFile 업로드할 파일
     * @param dirName S3 버킷 내에서 파일을 저장할 디렉토리 이름
     * @return 업로드된 파일의 S3 URL
     * @throws IOException 파일 처리 중 발생할 수 있는 예외
     */
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 1. 파일이 비어있는지 확인
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 2. 고유한 파일 이름 생성 (파일 덮어쓰기 방지)
        String originalFilename = multipartFile.getOriginalFilename();
        String uniqueFileName = dirName + "/" + UUID.randomUUID().toString() + "_" + originalFilename;

        // 3. S3에 업로드할 요청 객체 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket) // 사용할 버킷 이름
                .key(uniqueFileName) // S3에 저장될 파일의 전체 경로 및 이름
                .build();

        // 4. S3 클라이언트를 사용하여 파일 업로드
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        // 5. 업로드된 파일의 URL 반환
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(uniqueFileName)).toExternalForm();
    }
}