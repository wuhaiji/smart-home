package com.ecoeler.utils;


import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class UploadUtil {
    /**
     * 生成的目录级数
     */
    public static final int FILE_PATH_DEEP = 8;

    @Value("${upload.file.path.prefix}")
    public String prefixPath;

    /**
     * 文件上传生成路径
     *
     * @param fileName 文件名
     * @param upload   上传前缀路径
     * @return
     */
    public String getUploadPath(String fileName, String upload) {
        //根据文件名称,生成hash字符串,截取前8位
        //1k2k2k3l5l6k3h4n4h4hn4nu4--1/k/2k/2k/3/l/5/l
        StringBuilder hash = new StringBuilder(Integer.toHexString(fileName.hashCode()));
        while (hash.length() <= FILE_PATH_DEEP) {
            hash.append("0");
        }
        StringBuilder uploadBuilder = new StringBuilder(upload);
        for (int i = 0; i < hash.length(); i++) {
            uploadBuilder.append("/").append(hash.charAt(i));
        }
        upload = uploadBuilder.toString();
        return upload;
    }

    public String uploadFile(MultipartFile file) throws IOException {
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
        //生成多级目录  oName原名 LocalDate.now().toString() 文件夹以上传日期分类
        String path =  "/"+ LocalDate.now().toString() + "/";
        //如果传到其它的服务器就将 realPath 转成 prefixPath
        File fileNew = new File(prefixPath + path);
        if (!fileNew.exists()) {
            fileNew.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + extName;
        String url=prefixPath + path+newName;
        File file1=new File(url);
        FileUtils.copyInputStreamToFile(file.getInputStream(), file1);
        return url;
    }


}
