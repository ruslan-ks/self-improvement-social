package com.my.selfimprovement.util;

import lombok.Data;

import java.nio.file.Path;

/**
 * Represents a file loaded to memory
 */
@Data
public class LoadedFile {
    private final Path path;
    private final byte[] bytes;
}
