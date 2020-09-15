package com.ecoeler.service.impl;

import com.ecoeler.app.service.IEmailCodeService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.utils.AliMailUtil;
import com.ecoeler.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 阿里邮箱验证码服务
 * @author tang
 * @since 2020/9/14
 */
@Service
public class AliEmailCodeServiceImpl implements IEmailCodeService {

    @Autowired
    private AliMailUtil aliMailUtil;

    @Autowired
    private RedisUtil redisUtil;

    private Random random=new Random();

    String generateCode(){
        StringBuilder code= new StringBuilder();

        for(int i=0;i<6;i++){
            code.append( Math.abs(random.nextInt(10) ));
        }

        return code.toString();
    }

    @Override
    public void filter(String ip) {
        if(!redisUtil.setExIfAbsent("EMAIL_IP_FROM:"+ip," ",2L, TimeUnit.MINUTES)){
            throw new ServiceException(TangCode.CODE_LIMIT_EMAIL_ERROR);
        }
    }

    @Async
    @Override
    public void sendCode(String email) {
        String code=generateCode();
        redisUtil.setEx("ALI_EMAIL_CODE:"+email,code,5L,TimeUnit.MINUTES);
        aliMailUtil.sendCode(email,code);
    }

    @Override
    public boolean verify(String email, String code) {
        return code.equals(redisUtil.get("ALI_EMAIL_CODE:"+email));
    }
}
