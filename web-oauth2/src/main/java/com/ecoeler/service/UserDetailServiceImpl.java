package com.ecoeler.service;

import com.ecoeler.app.entity.WebUser;
import com.ecoeler.feign.WebUserService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 获取用户的细节
 * @author tang
 * @since 2020/9/7
 */
@Service("myUserDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private WebUserService webUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Result<WebUser> result = webUserService.getUser(username);
        WebUser user = result.getData();
        if(user==null){
            throw new UsernameNotFoundException("----user can't be found!");
        }

        return UserDetailsImpl.getUserDetail(user);
    }
}
