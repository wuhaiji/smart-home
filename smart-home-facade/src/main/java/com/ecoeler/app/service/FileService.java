package com.ecoeler.app.service;
import org.springframework.web.multipart.MultipartFile;
/**
 * 文件上传服务类
 * @author TangCX
 */
public interface FileService {
    /**
     * 文件上传
     * @param file 文件
     * @return 新文件的名称
     */
    String uploadFile(MultipartFile file);
}
