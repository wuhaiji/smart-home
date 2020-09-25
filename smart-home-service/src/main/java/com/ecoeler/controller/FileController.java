package com.ecoeler.controller;

import com.ecoeler.app.service.FileService;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 图片控制器控制器
 * </p>
 *
 * @author tangcx
 * @since 2020-09-10
 */
@Slf4j
@RestController
@RequestMapping("/web-file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件路径
     */
    @RequestMapping("/upload")
    public Result uploadFile(@RequestBody MultipartFile file) {
        log.info("smart-home-service->FileController->begin upload file");
        String url = fileService.uploadFile(file);
        return Result.ok(url);

    }

}
