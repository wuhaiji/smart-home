package com.ecoeler.service;

import com.ecoeler.app.entity.WebPermission;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限
 * @author tang
 * @since 2020/9/16
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    private String perm;

    private GrantedAuthorityImpl(WebPermission permission){
        this.perm=permission.getPermission();
    }

    public static Set<GrantedAuthority> getPerm(List<WebPermission> permissions){
        Set<GrantedAuthority> set=new HashSet<>();
        for (WebPermission permission : permissions) {
            set.add(new GrantedAuthorityImpl(permission));
        }
        return set;
    }

    @Override
    public String getAuthority() {
        return perm;
    }

    @Override
    public int hashCode() {
        return perm.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof GrantedAuthorityImpl) {
            return this.perm!=null && this.perm.equals(((GrantedAuthorityImpl) obj).getAuthority());
        }
        return false;
    }

}
