package com.ecoeler.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/21
 */

@Slf4j
@Controller
@SessionAttributes({"authorizationRequest"})
public class PageController {

    /**
     * 登录
     */
    @RequestMapping("/page/login")
    public String loginPage(HttpServletRequest request, HttpSession session) {
        log.info("会话：{}", JSON.toJSON(session).toString());
        String language = request.getLocale().getLanguage();
        log.info("browserLanguage is：{}", language);

        if (language.startsWith("zh")) {
            return "login-zh";
        } else if (language.startsWith("en")) {
            return "login-en";
        } else {
            return "login-en";
        }
    }

    /**
     * 自定义授权页面，注意：一定要在类上加@SessionAttributes({"authorizationRequest"})
     *
     * @param model   model
     * @param request request
     * @return String
     * @throws Exception Exception
     */
    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {

        @SuppressWarnings("unchecked")
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes"));
        List<String> scopeList = new ArrayList<>();
        if (scopes != null) {
            scopeList.addAll(scopes.keySet());
        }
        model.put("scopeList", scopeList);

        String language = request.getLocale().getLanguage();
        log.info("browserLanguage is：{}", language);
        if (language.startsWith("zh")) {
            return "authorize-zh";
        } else if (language.startsWith("en")) {
            return "authorize-en";
        } else {
            return "authorize-en";
        }
    }

}
