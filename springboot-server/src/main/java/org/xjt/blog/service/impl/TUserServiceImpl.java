package org.xjt.blog.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.entity.TPerms;
import org.xjt.blog.entity.TUser;
import org.xjt.blog.entity.TUserRole;
import org.xjt.blog.mapper.TUserMapper;
import org.xjt.blog.mapper.TUserRoleMapper;
import org.xjt.blog.service.TUserService;
import org.xjt.blog.utils.RespBean;

import java.util.List;

@Service
@Transactional
@Slf4j
public class TUserServiceImpl implements TUserService {
    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public RespBean register(TUser tUser) {
        //1、先检查用户名是否已存在
        if(!ObjectUtils.isEmpty(this.findByUserName(tUser.getUsername()))){
            return RespBean.warn("用户名已存在请重新输入");
        }
        //2、产生随机盐
        String ss = RandomUtil.randomString(6);
        log.warn("随机字符串为==="+ss);
        tUser.setSalt(ss);
        Md5Hash md5Hash = new Md5Hash(tUser.getPassword(), ss, 1024);
        tUser.setPassword(md5Hash.toHex());

        int insert = tUserMapper.insert(tUser);
        System.out.println(tUser);
        if(insert >=0){
            return RespBean.ok("用户创建成功username="+tUser.getUsername(),tUser);
        }else{
            return RespBean.error("创建失败！");
        }
    }

    @Override
    public TUser findByUserName(String username) {
        TUser tUser = tUserMapper.selectOne(new QueryWrapper<TUser>().eq("username", username));
        return tUser;
    }

    @Override
    public List<TPerms> findPermsListByRoleId(Long id) {
        return null;
    }

    @Override
    public TUser findRolesByUsername(String principal) {
        TUser tUser = tUserMapper.selectRolesByUsername(principal);
        return tUser;
    }

    @Override
    public RespBean getAllUser() {
        List<TUser> tUsers = tUserMapper.selectList(null);
        if(ObjectUtils.isEmpty(tUsers)){
            return RespBean.warn("还没有用户去创建一个吧！");
        }else{
            return RespBean.ok("获取用户ok！",tUsers);
        }
    }

    @Override
    public RespBean updateByUid(TUser tUser) {
        int update = tUserMapper.updateById(tUser);
        if(update<0){
            return RespBean.error("更新失败！");
        }else{
            return RespBean.ok("更新成功",tUser);
        }
    }

    @Autowired
    private TUserRoleMapper tUserRoleMapper;

    @Override
    public RespBean deleteUserById(String id) {
        try {
            //1、删除用户表 id=xxx的用户
            tUserMapper.deleteById(id);

            //2、删除用户角色表 userid=xxx的用户
            QueryWrapper<TUserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("userid",id);
            tUserRoleMapper.delete(wrapper);

            return RespBean.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error(e.getMessage());
        }
    }

    @Override
    public RespBean getUserListByName(String name) {
        QueryWrapper<TUser> wrapper = new QueryWrapper<>();
        wrapper.like("username",name)
                .or()
                .like("nickname",name);

        List<TUser> userList = tUserMapper.selectList(wrapper);

        if(ObjectUtils.isEmpty(userList)){
            return RespBean.warn("没有找到");
        }else{
            return RespBean.ok("ok",userList);
        }
    }
}
