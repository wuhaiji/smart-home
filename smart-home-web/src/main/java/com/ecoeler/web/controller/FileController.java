package com.ecoeler.web.controller;


import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.feign.FileService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传端口
 *
 * @author tangcx
 * @since 2020/10/19
 */
@Slf4j
@RestController
@RequestMapping("/web/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/go/fastDFS/upload")
    Result goFastDFSUploadFile(@RequestBody MultipartFile file) {
        ExceptionUtil.notNull(file,TangCode.CODE_FILE_EMPTY_ERROR);
        log.info("smart-home-web->DeviceTypeController->go-fastDFS begin upload file");
        return fileService.goFastDFSUploadFile(file);
    }
}
