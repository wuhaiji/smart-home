package com.ecoeler.cache;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.utils.RedisUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 缓存切面
 *
 * @author tang
 * @since 2020/8/11
 */
@Component
@Aspect
public class CacheAspect {

    private Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获得缓存 键
     *
     * @param joinPoint
     * @param template
     * @return
     * @throws Exception
     */
    private String getKey(JoinPoint joinPoint, String template) throws Exception {
        Template t = new Template(template, template, cfg);
        StringWriter result = new StringWriter();
        t.process(getParams(joinPoint), result);
        result.flush();
        result.close();
        return result.toString();
    }

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


    @Pointcut("@annotation(com.ecoeler.cache.SetCache)")
    public void setCachePointCut() {
    }

    @Around("setCachePointCut() && @annotation(setCache)")
    public Object process(ProceedingJoinPoint joinPoint, SetCache setCache) throws Throwable {
        String key = getKey(joinPoint, setCache.value());
        String value = redisUtil.get(key);
        if (value == null) {
            //调用方法获得值
            Object proceed = joinPoint.proceed();
            redisUtil.setIfAbsent(key, JSONObject.toJSONString(proceed));
            return proceed;
        } else {
            //否则直接返回
            Class<?> returnType = getReturnType(joinPoint);
            //说明是集合
            if (returnType == List.class) {
                return JSONArray.parseArray(value,getGenericReturnType(joinPoint));
            }
            return JSONObject.parseObject(value, getReturnType(joinPoint));
        }
    }

    @Pointcut("@annotation(com.ecoeler.cache.ClearCache)")
    public void clearCachePointCut() {
    }

    @Around("clearCachePointCut() && @annotation(clearCache)")
    public Object process(ProceedingJoinPoint joinPoint, ClearCache clearCache) throws Throwable {
        String[] values = clearCache.value();
        Object proceed = joinPoint.proceed();
        for (String value : values) {
            if (value.indexOf('$') == -1) {
                //说明是批量删
                redisUtil.delete(redisUtil.keys(value));
            } else {
                String key = getKey(joinPoint, value);
                redisUtil.delete(key);
            }
        }
        return proceed;
    }

}
