package com.ecoeler.permission;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Set;


/**
 * 权限 拦截器
 * @author tang
 * @since 2020/8/11
 */
@Component
public class PermLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IWebPermissionService iWebPermissionService;


    WebUser needLogin(HttpServletRequest request)  {
        String token = request.getParameter("token");
        if(token==null){
            throw new ServiceException(CommonCode.NULL_TOKEN);
        }
        String tokenInfo ;
        try {
            //在缓冲中查询当前用户
            tokenInfo =redisUtil.get (token);
            return JSONObject.parseObject(tokenInfo, WebUser.class);
        } catch (Exception e) {
            throw new ServiceException(CommonCode.TOKEN);
        }
    }

    /**
     * 拦截匹配用户权限
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            Limit limit = ((HandlerMethod) handler).getMethodAnnotation(Limit.class);
            //不需要登录
            if (limit != null && !limit.needLogin()) {
                return true;
            }
            //在缓存中拿取用户信息
            Long roleId = needLogin(request).getId();

            if (limit != null) {
                //拿取后台权限
                Set<String> permissions = iWebPermissionService.selectBackPermissionByRoleId(roleId);
                if (!permissions.containsAll(Arrays.asList(limit.hasPerm()))) {
                    throw new ServiceException(CommonCode.INSUFFICIENT_PERMISSIONS);
                }
            }
        }

        return true;
    }
}
