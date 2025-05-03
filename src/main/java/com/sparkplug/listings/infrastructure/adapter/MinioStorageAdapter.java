package com.sparkplug.listings.infrastructure.adapter;

import com.sparkplug.listings.application.port.StoragePort;
import com.sparkplug.listings.infrastructure.storage.MinioStorageService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class MinioStorageAdapter implements StoragePort {
    private final MinioStorageService storageService;

    public MinioStorageAdapter(MinioStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return storageService.uploadFile(file);
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        return storageService.uploadFiles(files);
    }
}