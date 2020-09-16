package com.ecoeler.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限
 * @author tang
 * @since 2020/9/16
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    private String perm;

    private GrantedAuthorityImpl(String permission){
        this.perm=permission;
    }

    public static Set<GrantedAuthority> getPerm(Set<String> permissions){
        Set<GrantedAuthority> set=new HashSet<>();
        for (String permission : permissions) {
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
