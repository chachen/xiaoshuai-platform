package io.github.chachen.platform.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorage {
    StoredFile save(MultipartFile file);

    Resource open(String key);

    void delete(String key);

    record StoredFile(String key, String originalName, long size, String contentType) {
    }

    record Resource(InputStream stream, String contentType, String name) {
    }
}
