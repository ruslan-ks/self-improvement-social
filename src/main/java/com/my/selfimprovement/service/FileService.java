package com.my.selfimprovement.service;

import com.my.selfimprovement.util.LoadedFile;
import com.my.selfimprovement.util.exception.FileUploadException;
import com.my.selfimprovement.util.exception.IllegalMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Predicate;

public interface FileService {

    /**
     * Saves specified file, so it can be accessed later
     * @param file file to be saved
     * @param userId file owner id
     * @param isMediaTypeAllowed accepts {@code file} mimetype, returns {@code true} if mimetype is allowed, otherwise
     *                          returns false
     * @return saved file location
     * @throws FileUploadException if IOException occurs when trying to copy the file
     * @throws IllegalMediaTypeException if {@code file} has not allowed media type
     * (if {@code isMimeTypeAllowed} returns {@code false} for extracted media type)
     * or if media type obtained from file name differs from the one obtained from file metadata
     */
    String saveToUploads(MultipartFile file, long userId, Predicate<MediaType> isMediaTypeAllowed) throws IOException;

    /**
     * Removes file form uploads
     * @param fileName file to be removed
     * @throws com.my.selfimprovement.util.exception.FileRemovalException if IOException occurs when trying to remove
     * the file
     */
    void removeFromUploads(String fileName);

    LoadedFile getLoadedFile(String fileName) throws IOException;

}
