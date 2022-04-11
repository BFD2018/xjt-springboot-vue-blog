package org.xjt.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
//@EnableCaching
public class StarLightBlogApplication {
    public static void main( String[] args ) {
        SpringApplication.run(StarLightBlogApplication.class,args);
        log.warn("登录小熊博客后台api测试端口  http://localhost:8000/");
        log.warn("登录小熊博客后台管理  http://localhost:8001/");
        log.warn("登录小熊博客前台页面  http://localhost:8002/");
    }
}
