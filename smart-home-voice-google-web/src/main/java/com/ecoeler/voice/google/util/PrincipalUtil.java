package com.ecoeler.voice.google.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

/**
 * principal工具
 *
 * @author tang
 * @since 2020/9/18
 */
public class PrincipalUtil {

    public static Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }

    public static Long getUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return Long.parseLong(authentication.getName());
    }


}
