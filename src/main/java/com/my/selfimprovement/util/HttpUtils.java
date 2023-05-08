package com.my.selfimprovement.util;

import com.my.selfimprovement.util.exception.FileException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpUtils {

    private HttpUtils() {}

    public static ResponseEntity<Resource> buildInlineFileResponse(LoadedFile file) {
        Resource avatarResource = new ByteArrayResource(file.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(getMediaType(file.getPath()))
                .body(avatarResource);
    }

    /**
     * Returns file MediaType based on file name
     * @param filePath file path
     * @return MediaType defined by file name
     * @throws FileException if IOException occurs
     */
    public static MediaType getMediaType(Path filePath) {
        try {
            return MediaType.parseMediaType(Files.probeContentType(filePath));
        } catch (IOException ex) {
            throw new FileException("Failed to parse media type. IOException occurred", ex);
        }
    }

}
