package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;               
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;

@Service
public class FileStorageService {

    private final Path uploadDir;

    @Autowired
    public FileStorageService(@Value("${app.file.upload-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot create upload directory", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (filename.contains("..")) {
                throw new RuntimeException("Invalid filename");
            }

            Path target = this.uploadDir.resolve(UUID.randomUUID() + "-" + filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return target.toString();

        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file.", ex);
        }
    }
}
