package com.ecoeler.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

@Configuration
@Slf4j
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(-1);
        registrationBean.setName("request-info-filter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(
                (ServletRequest request, ServletResponse response, FilterChain chain) -> {
                    log.info("请求进来了");
                    HttpServletRequest res = (HttpServletRequest) request;
                    log.info("请求url:{}", res.getRequestURL());
                    log.info("请求uri:{}", res.getRequestURI());
                    log.info("请求参数：{}", JSON.toJSONString(res.getParameterMap()));
                    //继续调用Filter链
                    chain.doFilter(request, response);
                });

        return registrationBean;
    }


}
