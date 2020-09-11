package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.mapper.WebPermissionMapper;
import com.ecoeler.app.service.IWebPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebPermissionServiceImpl extends ServiceImpl<WebPermissionMapper, WebPermission> implements IWebPermissionService {
    /**
     * 查询所菜单权限
     * @return
     */
    @Override
    public List<MenuWebPermissionBean> selectAllMenuPermission() {
        QueryWrapper<WebPermission> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("id","permission_name","parent_id");
        queryWrapper.eq("source_type",1);
        //查询所有菜单权限
        List<WebPermission> webPermissions = baseMapper.selectList(queryWrapper);
        if (webPermissions!=null&&webPermissions.size()!=0){
            List<Long> exit=new ArrayList<>();
            //可以用id获取到权限
            Map<Long,WebPermission> menuWebPermissionBeanMap=new HashMap<>();
            for (WebPermission webPermission : webPermissions) {
                menuWebPermissionBeanMap.put(webPermission.getId(),webPermission);
            }
            //封装结果
            List<MenuWebPermissionBean> result=new ArrayList<>();
            //封装无父菜单
            for (WebPermission webPermission : webPermissions) {
                if (menuWebPermissionBeanMap.get(webPermission.getId()).getParentId()==null){
                    MenuWebPermissionBean bean=new MenuWebPermissionBean();
                    bean.setId(webPermission.getId());
                    bean.setMenu(webPermission.getPermissionName());
                    result.add(bean);
                }
            }
            //封装子菜单
            for (WebPermission webPermission : webPermissions) {
                if (menuWebPermissionBeanMap.get(webPermission.getId()).getParentId()!=null){
                    WebPermission parent;
                }
            }


        }
        return null;
    }
}
