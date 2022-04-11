package org.xjt.blog.service;

import org.xjt.blog.utils.RespBean;

/**
 * @author xiong
 * @ClassName TRoleService.java
 * @createTime 2021/11/12
 * @Description TODO
 */
public interface TRoleService {
    RespBean getAllRoles();

    RespBean getUserCountsByRole();


}
