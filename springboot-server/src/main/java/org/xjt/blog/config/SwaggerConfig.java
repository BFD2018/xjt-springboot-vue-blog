package org.xjt.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xjt.blog.common.XiongBlogConfig;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /** 系统基础配置 */
    @Autowired
    private XiongBlogConfig xiongBlogConfig;

    /** 是否开启swagger */
    @Value("${swagger.enabled}")
    private boolean enabled;

    /** 设置请求的统一前缀 */
    @Value("${swagger.pathMapping}")
    private String pathMapping;

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enabled)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact(xiongBlogConfig.getName(), "https://github.com/BFD2018", "1351655382@qq.com");

        return new ApiInfo("Api Documentation",
                "小熊博客API文档",
                xiongBlogConfig.getVersion(),
                "http://localhost:8000",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
