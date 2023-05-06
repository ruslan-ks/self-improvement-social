package com.my.selfimprovement.service;

import com.my.selfimprovement.util.LoadedFile;
import com.my.selfimprovement.util.MediaTypeDetector;
import com.my.selfimprovement.util.exception.IllegalMediaTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocalFileService implements FileService {

    @Value("${files.upload.dir}")
    private String filesUploadDir;

    private final MediaTypeDetector mediaTypeDetector;

    @Override
    public String saveToUploads(MultipartFile file, long userId, Predicate<MediaType> isMediaTypeAllowed)
            throws IOException {
        String fileName = generateUniqueFileName(file, userId);

        throwIfMimeTypeIsIllegal(file, isMediaTypeAllowed);

        Path filePath = Paths.get(filesUploadDir, fileName).toAbsolutePath();
        try (var is = file.getInputStream()) {
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error("Failed to copy file. IOException occurred: ", ex);
            throw ex;
        }
        log.info("File uploaded: {}", filePath);
        return fileName;
    }

    private String generateUniqueFileName(MultipartFile file, long userId) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return "file" + userId + "_" + Instant.now().getEpochSecond() + "." + extension;
    }

    private void throwIfMimeTypeIsIllegal(MultipartFile file, Predicate<MediaType> isMediaTypeAllowed)
            throws IOException {
        // Check media type obtained from content type
        String fileContentType = file.getContentType();
        if (fileContentType == null) {
            throw new IllegalMediaTypeException("Media type cannot be detected: failed to obtain content type" +
                    " of file: " + file.getOriginalFilename());
        }
        MediaType contentTypeMediaType = MediaType.parseMediaType(fileContentType);
        if (!isMediaTypeAllowed.test(contentTypeMediaType)) {
            throw new IllegalMediaTypeException("File " + file.getOriginalFilename() + " has not allowed media type: " +
                    contentTypeMediaType);
        }

        MediaType fileMetadataMediaType = detectMediaTypeFromMetadata(file);

        // Check whether media type obtained from file metadata equals to the one obtained from content type
        if (!fileMetadataMediaType.equals(contentTypeMediaType)) {
            throw new IllegalMediaTypeException("Media type obtained from file content type is not equal to the one " +
                    "obtained from file metadata: " + contentTypeMediaType + " != " + fileMetadataMediaType);
        }
    }

    private MediaType detectMediaTypeFromMetadata(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            return mediaTypeDetector.detectByMetadata(is);
        } catch (IOException ex) {
            log.error("Failed to check mime type. Exception occurred: ", ex);
            throw ex;
        }
    }

    @Override
    public void removeFromUploads(String fileName) throws IOException {
        Path filePath = Paths.get(filesUploadDir, fileName);
        Files.delete(filePath);
    }

    @Override
    public LoadedFile getLoadedFile(String fileName) throws IOException {
        Path filePath = Paths.get(filesUploadDir, fileName);
        return new LoadedFile(filePath, Files.readAllBytes(filePath));
    }

}
