package org.xjt.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.entity.TRole;
import org.xjt.blog.mapper.TRoleMapper;
import org.xjt.blog.service.TRoleService;
import org.xjt.blog.utils.RespBean;

import java.util.List;
import java.util.Map;

@Service
public class TRoleServiceImpl implements TRoleService {
    @Autowired
    private TRoleMapper tRoleMapper;

    @Override
    public RespBean getAllRoles() {
        List<TRole> tRoleList = tRoleMapper.selectList(null);

        if(ObjectUtils.isEmpty(tRoleList)){
            return RespBean.warn("没找到任何角色");
        }else{
            return RespBean.ok("ok",tRoleList);
        }
    }

    @Override
    public RespBean getUserCountsByRole() {
        List<Map<String, Integer>> ret = tRoleMapper.getUserCountsGroupByRole();
        if(ObjectUtils.isEmpty(ret)){
            return RespBean.error("查询失败");
        }else{
            return RespBean.ok("ok",ret);
        }
    }
}
