package com.sparkplug.listings.application.port;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoragePort {

    String uploadFile(MultipartFile file);

    List<String> uploadFiles(List<MultipartFile> files);
}