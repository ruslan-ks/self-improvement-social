package com.my.selfimprovement.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * Saves specified file, so it can be accessed later
     * @param file file to be saved
     * @param userId file owner id
     * @return saved file location
     * @throws com.my.selfimprovement.util.exception.FileUploadException if IOException occurs when trying to copy
     * the file
     */
    String saveToUploads(MultipartFile file, long userId);

    /**
     * Removes file form uploads
     * @param fileName file to be removed
     * @throws com.my.selfimprovement.util.exception.FileRemovalException if IOException occurs when trying to remove
     * the file
     */
    void removeFromUploads(String fileName);

}
