package com.ecoeler.service.impl;

import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.service.FileService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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
    @Value("${goFastDFS.file.path}")
    public String goFastDFSPath;
    @Value("${goFastDFS.file.group}")
    public String goFastDFSGroup;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return
     */

    @Override
    public String goFastDFSUploadFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String newName="";
            if (filename!=null){
                newName=UUID.randomUUID()+filename.substring(filename.lastIndexOf("."));
            }
            InputStreamResource isr = new InputStreamResource(file.getInputStream(), newName);
            Map<String, Object> params = new HashMap<>(6);
            params.put("file", isr);
            params.put("path", "smartHome/"+LocalDate.now().toString());
            params.put("output", "json");
            String resp = HttpUtil.post(goFastDFSPath+goFastDFSGroup+"/upload", params);
            //log.info("resp:{}",resp);
            JSONObject jsonObject = JSONObject.parseObject(resp);
            return  jsonObject.getString("path");

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ServiceException(TangCode.FILE_NOT_EXISTS_ERROR);
        }
    }

    @Override
    public void goFastDFSDeleteFile(String path) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("path",path);
        String res = HttpUtil.post(goFastDFSPath+goFastDFSGroup+"/delete", params);
        //log.info("resp:{}",res);
        JSONObject jsonObject = JSONObject.parseObject(res);
        if (!"ok".equals(jsonObject.getString("status"))){
            log.error("goFastDFS 文件删除异常");
            throw new ServiceException(TangCode.FILE_DELETE_ERROR);
        }

    }
}
