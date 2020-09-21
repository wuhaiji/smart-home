package com.ecoeler.util;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * spring环境中获取request和session
 *
 * @author whj
 */

public class ServletUtil {

    private static final HttpServletRequest request;

    private static final HttpServletResponse response;

    static {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        response = (Objects.requireNonNull(requestAttributes)).getResponse();
        request = requestAttributes.getRequest();
    }


    public static HttpServletRequest getRequest() {
        return request;
    }

    public static HttpServletResponse getResponse() {
        return response;
    }


    public static HttpSession getSession() {
        return request.getSession(true);

    }

    public static Object getRequestAttribute(String attributeName) {
        return request.getAttribute(attributeName);
    }

    public static Object getSessionAttribute(String attributeName) {
        return request.getSession().getAttribute(attributeName);
    }

    public static void responseWriteJson(String data) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            response.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
