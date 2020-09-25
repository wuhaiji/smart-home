package com.ecoeler.voice.alexa.filter;

import com.alibaba.fastjson.JSON;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Configuration
@Slf4j
public class FilterConfig {

    /**
     * filter全局拦截异常，返回json
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);
        registrationBean.setName("request-info-filter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(
                (ServletRequest request, ServletResponse response, FilterChain chain) -> {
                    // HttpServletRequest res = (HttpServletRequest) request;
                    // log.info("请求进来了");
                    // if (!res.getRequestURI().contains("/static/")) {
                    //     log.info("请求进来了");
                    //     log.info("请求url:{}", res.getRequestURL());
                    //     log.info("请求uri:{}", res.getRequestURI());
                    //     log.info("请求参数：{}", JSON.toJSONString(res.getParameterMap()));
                    // }

                    //继续调用Filter链
                    try {
                        chain.doFilter(request, response);
                    } catch (Exception e) {
                        log.error("Global Service exception:", e);
                        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                        response.getWriter().write(JSON.toJSONString(Result.error(CommonCode.SERVER_ERROR.getCode(), e.getMessage())));
                    }
                });

        return registrationBean;
    }


}
