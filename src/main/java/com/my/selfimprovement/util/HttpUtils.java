package com.my.selfimprovement.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class HttpUtils {

    private HttpUtils() {}

    public static ResponseEntity<Resource> buildInlineFileResponse(LoadedFile file) {
        Resource avatarResource = new ByteArrayResource(file.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(file.getMediaType())
                .body(avatarResource);
    }

}
