package com.ecoeler.service.impl;

import com.ecoeler.app.service.FileService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 文件上传实现类
 * </p>
 *
 * @author tangcx
 * @since 2020-09-10
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
   @Autowired
   private UploadUtil uploadUtil;
    /**
     * 文件上传
     * @param file 文件
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return uploadUtil.uploadFile(file);
        } catch (IOException e) {
            log.error(e.getMessage(),e.getCause());
            throw new ServiceException(TangCode.CODE_UPLOAD_FILE_FAIL);
        }
    }
}
