package com.my.selfimprovement.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * Saves specified file, so it can be accessed later
     * @param file file to be saved
     * @param userId file owner id
     * @return saved file location
     */
    String saveToUploads(MultipartFile file, long userId);

}
