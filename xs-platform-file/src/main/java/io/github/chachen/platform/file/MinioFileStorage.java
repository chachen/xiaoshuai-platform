package io.github.chachen.platform.file;
import io.github.chachen.platform.web.exception.BusinessException;
import io.minio.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream; import java.util.*;
public class MinioFileStorage implements FileStorage {
    private final MinioClient client; private final FileProperties p;
    public MinioFileStorage(MinioClient client,FileProperties p){this.client=client;this.p=p;try{boolean exists=client.bucketExists(BucketExistsArgs.builder().bucket(p.getBucket()).build());if(!exists)client.makeBucket(MakeBucketArgs.builder().bucket(p.getBucket()).build());}catch(Exception e){throw new IllegalStateException("MinIO bucket 初始化失败",e);}}
    @Override public StoredFile save(MultipartFile file){if(file.isEmpty()||file.getSize()>p.getMaxSize())throw new BusinessException("FILE_TOO_LARGE","文件为空或超过大小限制");String original=Optional.ofNullable(file.getOriginalFilename()).orElse("file");String ext=original.contains(".")?original.substring(original.lastIndexOf('.')+1).toLowerCase():"";if(Arrays.stream(p.getAllowedExtensions()).noneMatch(ext::equals))throw new BusinessException("FILE_TYPE_NOT_ALLOWED","文件类型不允许");String key=UUID.randomUUID()+"."+ext;try(InputStream in=file.getInputStream()){client.putObject(PutObjectArgs.builder().bucket(p.getBucket()).object(key).stream(in,file.getSize(),-1).contentType(file.getContentType()).build());return new StoredFile(key,original,file.getSize(),file.getContentType());}catch(Exception e){throw new BusinessException("FILE_SAVE_FAILED","文件保存失败");}}
    @Override public Resource open(String key){try{if(key==null||key.contains("..")||key.contains("/")||key.contains("\\"))throw new BusinessException("FILE_KEY_INVALID","文件标识非法");StatObjectResponse stat=client.statObject(StatObjectArgs.builder().bucket(p.getBucket()).object(key).build());return new Resource(client.getObject(GetObjectArgs.builder().bucket(p.getBucket()).object(key).build()),stat.contentType(),key);}catch(BusinessException e){throw e;}catch(Exception e){throw new BusinessException("FILE_NOT_FOUND","文件不存在");}}
    @Override public void delete(String key){try{client.removeObject(RemoveObjectArgs.builder().bucket(p.getBucket()).object(key).build());}catch(Exception e){throw new BusinessException("FILE_DELETE_FAILED","文件删除失败");}}
}
