package org.xjt.blog.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //1.创建shiroFilter  //负责拦截所有请求
    @Bean(name="myShiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());

        //配置系统受限资源
        //配置系统公共资源
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/admin/**", "anon");      //测试专用
        map.put("/blog/**", "anon");      //测试专用
        map.put("/type/**", "anon");      //测试专用
        map.put("/comment/**", "anon");      //测试专用

        map.put("/classpath/**", "anon");      //测试专用
        map.put("/user/**", "anon"); //anon 设置为公共资源  放行资源放在下面
        map.put("/file/**", "anon"); //anon 设置为公共资源  放行资源放在下面

        map.put("/**", "authc");//authc 请求这个资源需要认证和授权

        //默认认证界面路径---当认证不通过时跳转
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean("myDefaultWebSecurityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator());

        List<Realm> realms = new ArrayList<>();
        //添加多个Realm
        realms.add(getCustomerRealm());
        realms.add(userEmailRealm());

        //给安全管理器设置
        defaultWebSecurityManager.setRealms(realms);
        // 记住我
        defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager());

        return defaultWebSecurityManager;
    }

    /**
     * 系统自带的Realm管理，主要针对多realm
     */
    @Bean("myModularRealmAuthenticator")
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        //自己重写的ModularRealmAuthenticator
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    //3.创建自定义realm
    @Bean("myCustomerRealm")
    public Realm getCustomerRealm() {
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

    //3.创建自定义realm
    @Bean("myUserEmailRealm")
    public Realm userEmailRealm() {
        UserEmailRealm emailRealm = new UserEmailRealm();
        return emailRealm;
    }

    /**
     * * 设置cookie 记住我生成cookie
     */
    @Bean("myCookieRememberMeManager")
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 设置cookie有效时间
     */
    @Bean("mySimpleCookie")
    public SimpleCookie rememberMeCookie() {
        /*这个参数是cookie的名称，对应前端页面的checkbox的name=remremberMe*/
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        /*cookie的有效时间为30天，单位秒*/
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
}
