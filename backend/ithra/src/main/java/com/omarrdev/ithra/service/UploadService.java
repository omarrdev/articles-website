package com.omarrdev.ithra.service;

import com.omarrdev.ithra.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024L;

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("File is empty");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BusinessException("Unsupported file type. Allowed: jpg, jpeg, png, webp");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BusinessException("File size exceeds 5MB limit");
        }

        String ext = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + ext;

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);
            Files.copy(file.getInputStream(), uploadPath.resolve(filename));
        } catch (IOException e) {
            throw new BusinessException("Failed to store file");
        }

        return baseUrl + "/uploads/" + filename;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "jpg";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
