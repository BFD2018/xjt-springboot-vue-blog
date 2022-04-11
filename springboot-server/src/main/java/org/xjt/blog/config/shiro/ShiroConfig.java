package org.xjt.blog.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //1.创建shiroFilter  //负责拦截所有请求
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager getDefaultWebSecurityManager){

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager);

        //配置系统受限资源
        //配置系统公共资源
        Map<String,String> map = new LinkedHashMap<>();
        map.put("/admin/**","anon");      //测试专用
        map.put("/blog/**","anon");      //测试专用
        map.put("/comment/**","anon");      //测试专用

        map.put("/classpath/**","anon");      //测试专用
        map.put("/user/**","anon"); //anon 设置为公共资源  放行资源放在下面
        map.put("/file/**","anon"); //anon 设置为公共资源  放行资源放在下面

        map.put("/**","authc");//authc 请求这个资源需要认证和授权

        //默认认证界面路径---当认证不通过时跳转
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean(name = "getDefaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getRealm") Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置
        defaultWebSecurityManager.setRealm(realm);

        return defaultWebSecurityManager;
    }

    //3.创建自定义realm
    @Bean(name = "getRealm")
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();

        //加盐
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        //开启缓存管理器
        customerRealm.setCachingEnabled(true);
        customerRealm.setCacheManager(new EhCacheManager());
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthenticationCacheName("AuthenticationCache");      //也可以不设置 有默认值 包名.authenticationCache
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthorizationCacheName("AuthorizationCache");

        return customerRealm;
    }
}
