package rkostiuk.selfimprovement.util;

import lombok.Data;
import org.springframework.http.MediaType;

import java.nio.file.Path;

/**
 * Represents a file loaded to memory
 */
@Data
public class LoadedFile {
    private final Path path;
    private final byte[] bytes;
    private final MediaType mediaType;
}
