package org.xjt.blog.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.xjt.blog.entity.TPerms;
import org.xjt.blog.entity.TRole;
import org.xjt.blog.entity.TUser;
import org.xjt.blog.service.TUserService;

import java.util.List;

@Slf4j
public class CustomerRealm extends AuthorizingRealm {
    @Autowired
    private TUserService tUserService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("======doGetAuthenticationInfo=======");

        String principal = (String) principals.getPrimaryPrincipal();
        TUser user = tUserService.findRolesByUsername(principal);

        if(!CollectionUtils.isEmpty(user.getRoles())){
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            //1、给认证用户添加角色信息
            for (TRole role : user.getRoles()) {
                authorizationInfo.addRole(role.getName());
                //2、给认证用户添加权限信息
                List<TPerms> permsList = tUserService.findPermsListByRoleId(role.getId());
                if(!CollectionUtils.isEmpty(permsList) && permsList.get(0) != null){
                    for (TPerms perms : permsList) {
                        authorizationInfo.addStringPermission(perms.getName());
                    }
                }
            }
            return authorizationInfo;
        }
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("======doGetAuthenticationInfo=======");

        //获取用户名
        String principal = (String) token.getPrincipal();
        log.debug("====用户名===="+principal);

        //假设是从数据库获得的 用户名，密码
        TUser tUser = tUserService.findByUserName(principal);
        log.debug("====认证的tUser===="+tUser);

        if (tUser.getUsername().equals(principal)){
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                    principal,
                    tUser.getPassword(),
                    new MyByteSource(tUser.getSalt()),
                    this.getName());
            return simpleAuthenticationInfo;
        }

        return null;
    }
}
