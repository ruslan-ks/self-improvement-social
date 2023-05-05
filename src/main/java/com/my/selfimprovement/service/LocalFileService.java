package com.my.selfimprovement.service;

import com.my.selfimprovement.util.exception.FileRemovalException;
import com.my.selfimprovement.util.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

@Service
@Slf4j
public class LocalFileService implements FileService {

    @Value("${files.upload.dir}")
    private String filesUploadDir;

    @Override
    public String saveToUploads(MultipartFile file, long userId) {
        String fileName = generateUniqueFileName(file, userId);
        Path fileStorage = Paths.get(filesUploadDir, fileName).toAbsolutePath();
        try {
            Files.copy(file.getInputStream(), fileStorage, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error("Failed to save file! exception: ", ex);
            throw new FileUploadException(ex);
        }
        log.info("File uploaded: {}", fileStorage);
        return fileName;
    }

    private String generateUniqueFileName(MultipartFile file, long userId) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return "file" + userId + "_" + Instant.now().getEpochSecond() + "." + extension;
    }

    @Override
    public void removeFromUploads(String fileName) {
        Path filePath = Paths.get(filesUploadDir, fileName);
        try {
            Files.delete(filePath);
        } catch (IOException ex) {
            log.error("Failed to remove file '{}'", filePath);
            log.error("Failed to remove file. IOException: ", ex);
            throw new FileRemovalException(ex);
        }
    }

}
