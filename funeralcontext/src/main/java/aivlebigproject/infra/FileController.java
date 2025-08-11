package aivlebigproject.infra;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileController {

    // Docker 컨테이너 내부의 공유 볼륨 경로
    private final String storagePath = "/app/storage/";

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        // 1. 파일 경로 조합
        String filePath = storagePath + fileName;
        Resource resource = new FileSystemResource(filePath);

        // 2. 파일이 존재하는지 확인
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 3. 파일의 Content-Type 결정 (예: image/png)
        String contentType = "application/octet-stream"; // 기본값
        if (fileName.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (fileName.endsWith(".pdf")) {
            contentType = MediaType.APPLICATION_PDF_VALUE;
        }

        // 4. 파일을 응답으로 반환
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
