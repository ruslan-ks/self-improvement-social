package rkostiuk.selfimprovement.util;

import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.util.i18n.UIMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

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

    public static ResponseEntity<ResponseBody> badRequest(String developerMessage) {
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .developerMessage(developerMessage)
                .build();
        return ResponseEntity.badRequest().body(responseBody);
    }

    public static ResponseEntity<ResponseBody> badRequest(UIMessage uiMessage) {
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(uiMessage)
                .build();
        return ResponseEntity.badRequest().body(responseBody);
    }

    public static ResponseEntity<ResponseBody> notFound(String developerMessage) {
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.NOT_FOUND)
                .developerMessage(developerMessage)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static <T> ResponseEntity<ResponseBody> page(String pageKey, Page<T> page) {
        long totalCount = page.getTotalElements();
        long pageNumber = page.getNumber();
        long pageSize = page.getSize();
        List<T> dtoList = page.toList();
        Map<String, ?> responseDataMap = Map.of(
                "count", totalCount,
                "page", pageNumber,
                "size", pageSize,
                pageKey, dtoList
        );
        return ResponseEntity.ok(ResponseBody.ok(responseDataMap));
    }

}
