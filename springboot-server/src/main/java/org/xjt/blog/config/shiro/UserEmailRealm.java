package org.xjt.blog.config.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xjt.blog.entity.TPerms;
import org.xjt.blog.entity.TRole;
import org.xjt.blog.entity.TUser;
import org.xjt.blog.service.TUserService;

import java.util.List;

public class UserEmailRealm extends AuthorizingRealm {
    @Autowired
    private TUserService tUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("————密码授权————doGetAuthorizationInfo————");
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

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("————邮箱登录认证————doGetAuthenticationInfo————");
        UserEmailToken userEmailToken = (UserEmailToken) token;
        String principal = (String) userEmailToken.getPrincipal();

        //根据邮箱查询用户
        TUser tUser = tUserService.findUserByEmail(principal);
        //因为没有密码，并且验证码在之前就验证了
        if (tUser == null) {
            throw new UnknownAccountException();
        }

        SecurityUtils.getSubject().getSession().setAttribute("login_user",tUser);

        return new SimpleAuthenticationInfo(
                principal,principal,this.getName()
        );
    }

    /**
     * 用来判断是否使用当前的 realm
     *
     * @param var1 传入的token
     * @return true就使用，false就不使用
     */
    @Override
    public boolean supports(AuthenticationToken var1) {
        return var1 instanceof HostAuthenticationToken;
    }
}
