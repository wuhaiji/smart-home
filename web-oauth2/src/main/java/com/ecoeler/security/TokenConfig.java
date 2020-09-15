package com.ecoeler.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * token 配置
 * @author tang
 * @since 2020/9/7
 */
@Configuration
public class TokenConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 令牌管理方式
     * @return
     */
    @Bean
    public TokenStore tokenStore(){
        //使用内存方式生成普通内存
        //return new InMemoryTokenStore();
        //使用redis来存储token信息
        return new RedisTokenStore(redisConnectionFactory);
    }


    /**
     * 授权码服务
     * @param dataSource 数据源
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource){
        //以内存方式管理授权码
        //return new InMemoryAuthorizationCodeServices();
        //以JDBC方式管理授权码，若使用内存方式管理则存在 单点问题 需要创建数据库表 oauth_code
        return new JdbcAuthorizationCodeServices(dataSource);
    }


}
