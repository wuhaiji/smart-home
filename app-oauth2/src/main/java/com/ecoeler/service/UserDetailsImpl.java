package com.ecoeler.service;

import com.ecoeler.app.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 用户细节
 * @author tang
 * @since 2020/9/10
 */
public class UserDetailsImpl implements UserDetails {

    private AppUser user;

    private UserDetailsImpl(AppUser user){
        this.user=user;
    }

    /**
     * 生成UserDetail
     * @param appUser
     * @return
     */
    public static UserDetails getUserDetail(AppUser appUser){
        return new UserDetailsImpl(appUser);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
