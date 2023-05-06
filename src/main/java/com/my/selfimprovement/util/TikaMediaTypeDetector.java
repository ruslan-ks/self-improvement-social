package com.my.selfimprovement.util;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class TikaMediaTypeDetector implements MediaTypeDetector {

    private final Tika tika;

    @Override
    public MediaType detectByMetadata(InputStream inputStream) throws IOException {
        return MediaType.parseMediaType(tika.detect(inputStream));
    }

}
