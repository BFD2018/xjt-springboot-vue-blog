package org.xjt.blog.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.xjt.blog.entity.TBlog;
import org.xjt.blog.service.TBlogService;
import org.xjt.blog.utils.RespBean;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/blog")
public class TBlogController {
    @Autowired
    private TBlogService tBlogService;

    @GetMapping("/getByPage")
    @ApiOperation("博客分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "当前页") ,
            @ApiImplicitParam(name = "size",value = "每页的数量"),
            @ApiImplicitParam(name = "published",value = "是否公开"),
            @ApiImplicitParam(name = "flag",value = "原创或转载"),
            @ApiImplicitParam(name = "share_statement",value = "草稿"),
            @ApiImplicitParam(name = "is_delete",value = "是否已删除"),
    })
    public RespBean getBlogsByPage(@RequestParam(value = "current",required = false,defaultValue = "1") Integer current,
                                 @RequestParam(value = "size",required = false,defaultValue = "6") Integer size,
                                 @RequestParam(value = "published",required = false) Boolean published,
                                 @RequestParam(value = "flag",required = false) String flag,
                                 @RequestParam(value = "share_statement",required = false) Boolean share_statement,
                                 @RequestParam(value = "is_delete",required = false) Boolean is_delete){
        log.info("current="+current);
        log.info("size="+size);
        return tBlogService.getBlogsByPage(current, size,published,flag,share_statement,is_delete);
    }

    @GetMapping("/getByPageHelper")
    @ApiOperation("前台-博客分页查询,通过mybatis的分页插件PageHelper")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "当前页") ,
            @ApiImplicitParam(name = "size",value = "每页的数量"),
            @ApiImplicitParam(name = "published",value = "是否公开"),
            @ApiImplicitParam(name = "flag",value = "原创或转载"),
            @ApiImplicitParam(name = "share_statement",value = "草稿"),
            @ApiImplicitParam(name = "is_delete",value = "是否已删除"),
    })
    public RespBean getBlogsByPageHelper(@RequestParam(value = "current",defaultValue = "1") Integer current,
                                   @RequestParam(value = "size",defaultValue = "6") Integer size,
                                   @RequestParam(value = "type_id",required = false) String type_id,
                                   @RequestParam(value = "published",required = false) Boolean published,
                                   @RequestParam(value = "flag",required = false) String flag,
                                   @RequestParam(value = "share_statement",required = false) Boolean share_statement,
                                   @RequestParam(value = "is_delete",required = false) Boolean is_delete){
        log.info("current="+current);
        log.info("size="+size);
        return tBlogService.getBlogsByPageHelper(current, size,type_id,published,flag,share_statement,is_delete);
    }

    //根据博客总数量
    @GetMapping("/all/counts")
    public RespBean getBlogAllCounts(){
        return tBlogService.getBlogAllCounts();
    }

    //根据所有博客title获取词云图
    @GetMapping("/all/wordcloud")
    public RespBean getAllBlogTitleToWordCloud(){
        return tBlogService.getAllBlogTitleToWordCloud();
    }

    //根据博客标题模糊查询
    @GetMapping("/findByLikeTitle")
    @ApiOperation("通过博客标题查询")
    @ApiImplicitParam(name = "title",value = "博客文章的标题")
    public RespBean findBlogByTitle(@RequestParam("title") String title){
        return tBlogService.getBlogByTitle(title);
    }

    //保存文章和标签
    @PostMapping("/save")
    @ApiOperation("通过博客标题查询")
    @ApiImplicitParam(name = "title",value = "博客文章的标题")
    public RespBean saveBlogTag(@RequestBody HashMap<String,Object> params){
        System.out.println(params);

        return tBlogService.saveBlog(params);
    }

    //更新博客
    @PostMapping("/update")
    @ApiOperation("通过博客bid更新")
    @ApiImplicitParam(name = "bid",value = "通过博客bid更新")
    public RespBean updateBlogTag(@RequestBody HashMap<String, Object> params){
        System.out.println(params);

        return tBlogService.updateBlog(params);
    }

    //通过id查询
    @GetMapping("getById")
    @ApiOperation("通过bid查询")
    @ApiImplicitParam(name = "bid",value = "通过bid查询")
    public RespBean getBlogById(@RequestParam("bid") String bid){
        return tBlogService.getBlogById(bid);
    }

    //通过id查询博客详情
    @GetMapping("/detail")
    @ApiOperation("前台-通过id查询博客详情")
    @ApiImplicitParam(name = "bid",value = "前台-通过id查询博客详情")
    public RespBean getBlogDetailById(@RequestParam("blog_id") String blog_id){
        log.warn("blog_id=="+blog_id);
        return tBlogService.getBlogDetailById(blog_id);
    }

    //通过id删除
    @DeleteMapping("/delete")
    @ApiOperation("通过id删除博客")
    @ApiImplicitParam(name = "id",value = "通过id删除博客")
    public RespBean deleteBlogById(@RequestParam("bid") String bid){
        return tBlogService.deleteBlogById(bid);
    }

    //type对应的blog数量
    @GetMapping("/counts/byType")
    public RespBean getBlogCountsByType(){
        return tBlogService.getBlogCountsByType();
    }

}
