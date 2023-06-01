package rkostiuk.selfimprovement.service;

import rkostiuk.selfimprovement.util.LoadedFile;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import rkostiuk.selfimprovement.util.exception.FileException;
import rkostiuk.selfimprovement.util.exception.IllegalMediaTypeException;

import java.util.function.Predicate;

public interface FileService {

    /**
     * Saves specified file, so it can be accessed later
     * @param file file to be saved
     * @param userId file owner id
     * @param isMediaTypeAllowed accepts {@code file} mimetype, returns {@code true} if mimetype is allowed, otherwise
     *                          returns false
     * @return saved file location
     * @throws IllegalMediaTypeException if {@code file} has not allowed media
     * type(if {@code isMimeTypeAllowed} returns {@code false} for extracted media type)
     * or if media type obtained from file name differs from the one obtained from file metadata
     * @throws FileException if file cannot be saved
     * (for example if IOException occurs)
     */
    @PreAuthorize("isAuthenticated()")
    String saveToUploads(MultipartFile file, long userId, Predicate<MediaType> isMediaTypeAllowed);

    /**
     * Removes file form uploads
     * @param fileName file to be removed
     * @throws FileException if file cannot be removed
     * (for example if IOException occurs)
     */
    @PreAuthorize("isAuthenticated()")
    void removeFromUploads(String fileName);

    /**
     * Returns LoadedFile object containing file data
     * @param fileName file to be loaded
     * @return {@link LoadedFile} containing file data
     * @throws FileException if file cannot be loaded
     * (for example if IOException occurrs)
     */
    LoadedFile getLoadedFile(String fileName);

}
