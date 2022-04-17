package org.xjt.blog.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.List;
import java.util.stream.Collectors;

public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        assertRealmsConfigured();
        //依据Realm中配置的支持Token来进行过滤
        List<Realm> realms = this.getRealms()
                .stream()
                .filter(realm -> realm.supports(authenticationToken))
                .collect(Collectors.toList());
        if (realms.size() == 1) {
            return doSingleRealmAuthentication(realms.get(0), authenticationToken);
        } else {
            return doMultiRealmAuthentication(realms, authenticationToken);
        }
    }

}

