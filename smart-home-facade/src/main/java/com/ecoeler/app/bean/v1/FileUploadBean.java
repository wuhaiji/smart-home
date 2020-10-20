package com.ecoeler.app.bean.v1;

import lombok.Data;


/**
 * @author tangcx
 */
@Data
public class FileUploadBean{

    /**
     * 路径
     */
    private String url;
    /**
     * MD5
     */
    private String goFastDFSMD5;

    public FileUploadBean() {
    }

    public FileUploadBean(String url, String goFastDFSMD5) {
        this.url = url;
        this.goFastDFSMD5 = goFastDFSMD5;
    }
}
