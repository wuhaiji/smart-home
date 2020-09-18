package com.ecoeler.app.utils;

import java.security.Principal;

/**
 * principal工具
 * @author tang
 * @since 2020/9/18
 */
public class PrincipalUtil {

    public static Long getUserId(Principal principal){
        return Long.parseLong(principal.getName());
    }

}
