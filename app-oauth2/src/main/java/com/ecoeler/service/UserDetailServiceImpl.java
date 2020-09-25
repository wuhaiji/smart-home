package com.ecoeler.service;

import com.ecoeler.app.entity.AppUser;
import com.ecoeler.feign.AppUserService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 获取用户的细节
 * @author tang
 * @since 2020/9/7
 */
@Service("myUserDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Result<AppUser> result = appUserService.getUser(username);
        AppUser user = result.getData();
        if(user==null){
            throw new UsernameNotFoundException("----user can't be found!");
        }

        return UserDetailsImpl.getUserDetail(user);
    }
}
