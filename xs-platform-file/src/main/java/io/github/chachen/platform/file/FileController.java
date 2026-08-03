package io.github.chachen.platform.file;

import io.github.chachen.platform.web.result.ApiResult;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileStorage storage;

    public FileController(FileStorage s) {
        storage = s;
    }

    @PostMapping
    public ApiResult<FileStorage.StoredFile> upload(@RequestParam MultipartFile file) {
        return ApiResult.success(storage.save(file));
    }

    @GetMapping("/{key}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String key) {
        var r = storage.open(key);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(r.contentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : r.contentType())).body(new InputStreamResource(r.stream()));
    }

    @DeleteMapping("/{key}")
    public ApiResult<Void> delete(@PathVariable String key) {
        storage.delete(key);
        return ApiResult.success();
    }
}
