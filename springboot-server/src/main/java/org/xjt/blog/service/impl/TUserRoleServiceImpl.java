package org.xjt.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.xjt.blog.entity.TUserRole;
import org.xjt.blog.mapper.TUserRoleMapper;
import org.xjt.blog.service.TUserRoleService;
import org.xjt.blog.utils.RespBean;

@Service
public class TUserRoleServiceImpl implements TUserRoleService {
    @Autowired
    private TUserRoleMapper tUserRoleMapper;

    @Override
    public RespBean add(TUserRole tUserRole) {
        int insert = tUserRoleMapper.insert(tUserRole);
        if(insert<0){
            return RespBean.error("用户角色表添加一条记录失败");
        }else{
            return RespBean.ok("用户角色表添加一条记录成功");
        }
    }
}
