package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.app.dto.v1.PermDto;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebPermissionMapper;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.service.IWebRolePermissionService;
import com.ecoeler.cache.ClearCache;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Slf4j
@Service
public class WebRolePermissionServiceImpl extends ServiceImpl<WebRolePermissionMapper, WebRolePermission> implements IWebRolePermissionService {





}
