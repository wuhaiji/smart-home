package com.ecoeler.service.impl;

import com.ecoeler.app.service.IEmailCodeService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.utils.AliMail;
import com.ecoeler.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 阿里邮箱验证码服务
 * @author tang
 * @since 2020/9/14
 */
@Service
public class AliEmailCodeServiceImpl implements IEmailCodeService {

    @Autowired
    private AliMail aliMail;

    @Autowired
    private RedisUtil redisUtil;

    String generateCode(){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<6;i++){
            stringBuilder.append(Math.random()*10);
        }
        return stringBuilder.toString();
    }

    @Override
    public void filter(String ip) {
        if(!redisUtil.setExIfAbsent("EMAIL_IP_FROM:"+ip," ",2L, TimeUnit.MINUTES)){
            throw new ServiceException(TangCode.CODE_LIMIT_EMAIL_ERROR);
        }
    }

    @Override
    public void sendCode(String email) {
        String code=generateCode();
        redisUtil.setEx("ALI_EMAIL_CODE:"+email,code,5L,TimeUnit.MINUTES);
        aliMail.sendCode(email,code);
    }

    @Override
    public boolean verify(String email, String code) {
        return code.equals(redisUtil.get("ALI_EMAIL_CODE:"+email));
    }
}
