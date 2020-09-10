package com.ecoeler.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

/**
 * oauth2 配置
 * 配置OAuth2.0授权服务
 * @author tang
 * @since 2020/9/7
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Server extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    /**
     * 用户名密码模式下 需要配置该对象
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 授权码模式下 需要配置该对象
     */
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;


    /**
     * 加密方式(client_secret 数据库中的加密方式)
     */
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 注意 多数据源环境下 dataSource的名称
     * @return
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource){
        // 使用JdbcClientDetailsService，需要创建其特定的表 oauth_client_details
        JdbcClientDetailsService service=new JdbcClientDetailsService(dataSource);
        // 表中client_secret 的加密方式，默认使用 BCrypt
        service.setPasswordEncoder(passwordEncoder);
        return service;
    }

    /**
     * 用来配置客户端详情服务ClientDetailsService
     * 客户端详情信息在这里进行初始化（可以从数据库中获取）
     * 指定token服务支持哪些客户端来请求
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //使用数据库方式来存储客户端的信息
        clients.withClientDetails(jdbcClientDetailsService);
        /*clients.inMemory()//表示使用内存来存储客户端信息
                .withClient("c1")//客户端ID
                .secret(new BCryptPasswordEncoder().encode("secret"))//客户端密钥
                .resourceIds("rs1","rs2")//指定将来客户端可以访问资源列表
                .authorizedGrantTypes(//允许客户端的授权类型
                        "authorization_code",//授权码模式
                        "password",
                        "client_credentials",
                        "implicit",//隐式授权模式
                        "refresh_token"
                )
                .scopes("all")//允许授权的范围，这个all是一个标识，并不是所有的意思
                .autoApprove(false)//false 如果是授权码模式 则会跳转到授权页面
                .redirectUris("http://www.baidu.com");//授权码回调的地址*/
    }


    /**
     * 令牌管理服务
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        DefaultTokenServices service=new DefaultTokenServices();
        //客户端信息服务
        service.setClientDetailsService(clientDetailsService);
        //是否产生刷新令牌
        service.setSupportRefreshToken(true);
        //令牌存储策略
        service.setTokenStore(tokenStore);
        //令牌默认有效期
        service.setAccessTokenValiditySeconds(7200);
        //刷新令牌默认有效期3天
        service.setRefreshTokenValiditySeconds(259200);
        return service;
    }

    /**
     * token服务 要颁发令牌
     * 配置申请令牌的URL
     * 令牌的生成规则
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                ////密码 模式 所需服务
                .authenticationManager(authenticationManager)
                //授权码模式 所需服务
                .authorizationCodeServices(authorizationCodeServices)
                //令牌服务(不管什么模式都需要)
                .tokenServices(tokenService())
                //允许POST表单提交返回令牌
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }


    /**
     * 针对令牌安全策略
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //提供公钥密钥 jwt /oauth/token_key 接口公开
                .tokenKeyAccess("permitAll()")
                //检测令牌/oauth/check_token 接口公开
                .checkTokenAccess("permitAll()")
                //允许表单认证，申请令牌
                .allowFormAuthenticationForClients();
    }
}
