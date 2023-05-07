package com.my.selfimprovement.util;

import lombok.SneakyThrows;
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

    @SneakyThrows
    public static ResponseEntity<Resource> buildInlineFileResponse(LoadedFile file) {
        Resource avatarResource = new ByteArrayResource(file.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(getMediaType(file.getPath()))
                .body(avatarResource);
    }

    public static MediaType getMediaType(Path filePath) throws IOException {
        return MediaType.parseMediaType(Files.probeContentType(filePath));
    }

}
