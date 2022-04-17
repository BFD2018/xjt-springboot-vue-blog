package org.xjt.blog.service;

import org.xjt.blog.entity.TPerms;
import org.xjt.blog.entity.TUser;
import org.xjt.blog.utils.RespBean;

import java.util.List;

/**
 * @author xiong
 * @ClassName TUserService.java
 * @createTime 2021/11/10
 * @Description TODO
 */
public interface TUserService {
    //注册用户
    RespBean register(TUser tUser);

    TUser findByUserName(String username);

    List<TPerms> findPermsListByRoleId(Long id);

    TUser findRolesByUsername(String principal);

    RespBean getAllUser();

    RespBean updateByUid(TUser tUser);

    RespBean deleteUserById(String id);

    RespBean getUserListByName(String name);

    TUser findUserByEmail(String email);
}
