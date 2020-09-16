package com.ecoeler.service;

import com.ecoeler.app.entity.WebUser;
import com.ecoeler.feign.WebUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 用户细节
 * @author tang
 * @since 2020/9/10
 */
public class UserDetailsImpl implements UserDetails {

    private WebUserService webUserService;

    private WebUser user;

    private UserDetailsImpl(WebUser user,WebUserService webUserService){
        this.user=user;
        this.webUserService=webUserService;
    }

    /**
     * 生成UserDetail
     * @param user
     * @return
     */
    public static UserDetails getUserDetail(WebUser user,WebUserService webUserService){
        return new UserDetailsImpl(user,webUserService);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return GrantedAuthorityImpl.getPerm(webUserService.getPerm(user.getId()).getData());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
