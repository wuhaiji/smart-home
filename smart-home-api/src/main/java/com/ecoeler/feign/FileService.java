package com.ecoeler.feign;

import com.ecoeler.config.FileUploadFeignConfig;
import com.ecoeler.model.response.Result;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传
 *
 * @author TangCX
 */
@FeignClient(value = "smart-home-service", path = "/file",contextId = "file",configuration = FileUploadFeignConfig.class)
public interface FileService {
    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件路径
     */
    @RequestMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result uploadFile(@RequestBody MultipartFile file);

    @RequestMapping(value = "/go-fastDFS/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result goFastDFSUploadFile(@RequestBody MultipartFile file);

    /**
     * 删除图片
     *
     * @return 删除文件
     */
    @RequestMapping(value = "/go-fastDFS/delete")
    Result goFastDFSDeleteFile(@RequestParam String md5);

    class MyConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
