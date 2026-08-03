package io.github.chachen.platform.file;

import io.github.chachen.platform.web.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class LocalFileStorage implements FileStorage {
    private final FileProperties p;
    private final Path root;

    public LocalFileStorage(FileProperties p) {
        this.p = p;
        root = Path.of(p.getRoot()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public StoredFile save(MultipartFile f) {
        if (f.isEmpty() || f.getSize() > p.getMaxSize())
            throw new BusinessException("FILE_TOO_LARGE", "文件为空或超过大小限制");
        String name = Optional.ofNullable(f.getOriginalFilename()).orElse("file");
        String ext = name.contains(".") ? name.substring(name.lastIndexOf('.') + 1).toLowerCase() : "";
        if (Arrays.stream(p.getAllowedExtensions()).noneMatch(ext::equals))
            throw new BusinessException("FILE_TYPE_NOT_ALLOWED", "文件类型不允许");
        String key = UUID.randomUUID() + "." + ext;
        try {
            Files.copy(f.getInputStream(), root.resolve(key));
            return new StoredFile(key, name, f.getSize(), f.getContentType());
        } catch (IOException e) {
            throw new BusinessException("FILE_SAVE_FAILED", "文件保存失败");
        }
    }

    @Override
    public Resource open(String key) {
        try {
            Path path = safe(key);
            return new Resource(Files.newInputStream(path), Files.probeContentType(path), path.getFileName().toString());
        } catch (IOException | RuntimeException e) {
            throw new BusinessException("FILE_NOT_FOUND", "文件不存在");
        }
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(safe(key));
        } catch (IOException e) {
            throw new BusinessException("FILE_DELETE_FAILED", "文件删除失败");
        }
    }

    private Path safe(String key) {
        if (key == null || key.contains("..") || key.contains("/") || key.contains("\\"))
            throw new BusinessException("FILE_KEY_INVALID", "文件标识非法");
        return root.resolve(key).normalize();
    }
}
