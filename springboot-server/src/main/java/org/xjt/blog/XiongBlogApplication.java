package org.xjt.blog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@MapperScan("org.xjt.blog.mapper")  //mapper扫描包
@EnableTransactionManagement        //开启事务
@SpringBootApplication
@EnableCaching      //开启基于注解的缓存
public class XiongBlogApplication {
    public static void main( String[] args ) {
        SpringApplication.run(XiongBlogApplication.class,args);
        log.warn("登录小熊博客后台api测试端口  http://localhost:8000/");
        log.warn("登录小熊博客后台管理  http://localhost:8001/");
        log.warn("登录小熊博客前台页面  http://localhost:8002/");
    }
}
