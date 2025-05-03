package com.sparkplug.listings.infrastructure.storage;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MinioStorageService {

    private final MinioClient minioClient;
    private final String bucket;
    private final String endpoint;

    public MinioStorageService(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket}") String bucket
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
        this.endpoint = endpoint;
        createBucketIfNotExists();
    }

    private void createBucketIfNotExists() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize storage", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            String filename = generateFilename(LocalDateTime.now().toString());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return String.format("%s/%s/%s", endpoint, bucket, filename);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> uploadFile(file)))
                .toList()
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private String generateFilename(String postfix) {
        return UUID.randomUUID() + "_" + postfix;
    }
}