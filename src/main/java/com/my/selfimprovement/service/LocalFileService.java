package com.my.selfimprovement.service;

import com.my.selfimprovement.util.LoadedFile;
import com.my.selfimprovement.util.MediaTypeDetector;
import com.my.selfimprovement.util.exception.FileRemovalException;
import com.my.selfimprovement.util.exception.FileUploadException;
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
    public String saveToUploads(MultipartFile file, long userId, Predicate<MediaType> isMediaTypeAllowed) {
        String fileName = generateUniqueFileName(file, userId);

        throwIfMimeTypeIsIllegal(file, isMediaTypeAllowed);

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

    private void throwIfMimeTypeIsIllegal(MultipartFile file, Predicate<MediaType> isMediaTypeAllowed) {
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

    private MediaType detectMediaTypeFromMetadata(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return mediaTypeDetector.detectByMetadata(is);
        } catch (IOException ex) {
            log.error("Failed to check mime type. Exception occurred: ", ex);
            throw new FileUploadException(ex);
        }
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

    @Override
    public LoadedFile getLoadedFile(String fileName) throws IOException {
        Path filePath = Paths.get(filesUploadDir, fileName);
        return new LoadedFile(filePath, Files.readAllBytes(filePath));
    }

}
