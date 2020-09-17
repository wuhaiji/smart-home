package com.ecoeler.feign;

import com.ecoeler.app.entity.WebUser;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * web user
 * @author tang
 * @since 2020/9/16
 */
@FeignClient(value = "smart-home-service", path = "/web-user",contextId = "web")
public interface WebUserService {

    /**
     * 根据账号信息查询USER
     * @param account 可能为邮箱、可能为手机号
     * @return
     */
    @PostMapping("/user")
    Result<WebUser> getUser(@RequestParam String account);

    /**
     * 获得用户的权限列表
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/perm")
    Result<Set<String>> getPerm(@RequestParam Long userId);

//    /**
//     * 添加用户
//     * @param user 用户信息
//     * @return
//     */
//    @PostMapping("/add/user")
//    Result addUser(@RequestBody WebUser user);
//
//    /**
//     * 删除用户
//     * @param userId 用户ID
//     * @return
//     */
//    @PostMapping("/delete/user")
//    Result deleteUser(@RequestParam Long userId);


}
