package com.mna.springbootsecurity.file.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Service
@Slf4j
public class FileConvertor {

    @Value("${application.file.upload-dir}")
    private String uploadDir;

    public File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            log.error("MultipartFile is null or empty");
            throw new IllegalArgumentException("The provided file is null or empty.");
        }

        if (uploadDir == null || uploadDir.isEmpty()) {
            log.error("Upload directory is not specified");
            throw new IllegalStateException("Upload directory is not specified.");
        }

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT).format(new Date());
        String customDirPath = uploadDir + File.separator + "temp" + File.separator + timestamp;

        File customDir = new File(customDirPath);
        if (!customDir.exists()) {
            boolean dirsCreated = customDir.mkdirs();
            if (!dirsCreated) {
                log.error("Failed to create directory: {}", customDirPath);
                throw new IOException("Failed to create directory: " + customDirPath);
            }
        }

        File uploadedFile = new File(customDir, Objects.requireNonNull(multipartFile.getOriginalFilename()));

        try {
            multipartFile.transferTo(uploadedFile);
        } catch (IOException e) {
            log.error("Failed to transfer file: {}", multipartFile.getOriginalFilename(), e);
            throw new IOException("Failed to transfer file: " + multipartFile.getOriginalFilename(), e);
        }


        return uploadedFile;
    }
}
