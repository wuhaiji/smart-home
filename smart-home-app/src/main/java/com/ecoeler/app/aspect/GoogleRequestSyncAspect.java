package com.ecoeler.app.aspect;


import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.service.AppVoiceService;
import com.ecoeler.app.utils.PrincipalUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * 缓存切面
 *
 * @author tang
 * @since 2020/8/11
 */
@Component
@Aspect
public class GoogleRequestSyncAspect {

    @Autowired
    AppVoiceService appVoiceService;

    private static final Logger log = LoggerFactory.getLogger(GoogleRequestSync.class);

    /**
     * 获得切点入参
     *
     * @param joinPoint
     * @return
     */
    private Map<String, Object> getParams(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Map<String, Object> params = new HashMap<>(4);
        for (int i = 0; i < args.length; i++) {
            params.put(parameters[i].getName(), args[i]);
        }
        return params;
    }

    /**
     * 获得切点返回值
     *
     * @param joinPoint
     * @return
     */
    private Class<?> getReturnType(JoinPoint joinPoint) throws ClassNotFoundException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getReturnType();
    }

    /**
     * 获取List的泛型类型
     *
     * @param joinPoint
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> getGenericReturnType(JoinPoint joinPoint) throws ClassNotFoundException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            //因为list泛型只有一个值 所以直接取0下标
            String typeName = actualTypeArguments[0].getTypeName();
            //真实返回值类型 Class对象
            return Class.forName(typeName);
        }
        return null;
    }

    @Around("@annotation(com.ecoeler.app.aspect.GoogleRequestSync)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        //1. 执行方法
        Object result = joinPoint.proceed();
        // //2. 请求同步
        appVoiceService.requestSync(UserVoiceDto.of().setUserId(PrincipalUtil.getUserId()));
        return result;
    }

}
