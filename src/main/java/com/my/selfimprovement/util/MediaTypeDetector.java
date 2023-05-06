package com.my.selfimprovement.util;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public interface MediaTypeDetector {

    /**
     * Defines media type based on file metadata
     * @param inputStream file data presented as stream
     * @return file mime type
     */
    MediaType detectByMetadata(InputStream inputStream) throws IOException;

}
