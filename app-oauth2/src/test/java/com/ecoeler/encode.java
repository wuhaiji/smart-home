package com.ecoeler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class encode {

    @Test
    public void test(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        log.info(bCryptPasswordEncoder.encode("123456" ));
    }

}
