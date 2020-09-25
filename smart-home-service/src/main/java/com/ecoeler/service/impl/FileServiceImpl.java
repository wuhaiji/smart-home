package com.ecoeler.service.impl;

import com.ecoeler.app.service.FileService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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

    @Value("${upload.file.path.prefix}")
    public String prefixPath;
    @Value("${upload.file.url}")
    public String prefixUrl;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            if (file == null) {
                throw new ServiceException(TangCode.CODE_FILE_EMPTY_ERROR);
            }
            //原名
            String oName = Optional.ofNullable(file.getOriginalFilename()).orElse("");
            //后缀/
            String extName = "";
            if (!"".equals(oName.trim()) && oName.lastIndexOf(".") != -1) {
                extName = oName.substring(oName.lastIndexOf("."));
            }
            //  oName原名 LocalDate.now().toString() 文件夹以上传日期分类
            String dateString = LocalDate.now().toString() + "/";
            File fileNew = new File(prefixPath + dateString);
            if (!fileNew.exists()) {
                fileNew.mkdirs();
            }
            //生成新名字
            String newName = UUID.randomUUID().toString() + extName;
            String path = prefixPath + dateString + newName;
            File file1 = new File(path);
            file.transferTo(file1);
            //FileUtils.copyInputStreamToFile(file.getInputStream(), file1);
            return prefixUrl + dateString + newName;
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(TangCode.CODE_UPLOAD_FILE_FAIL);
        }
    }
}
