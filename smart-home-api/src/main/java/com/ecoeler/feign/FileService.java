package com.ecoeler.feign;

import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传
 *
 * @author TangCX
 */
@FeignClient(value = "smart-home-service", path = "/file",contextId = "file")
public interface FileService {
    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件路径
     */
    @RequestMapping("/upload")
    Result uploadFile(@RequestBody MultipartFile file);
}
