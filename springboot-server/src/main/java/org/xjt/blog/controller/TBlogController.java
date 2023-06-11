package org.xjt.blog.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.xjt.blog.entity.TBlog;
import org.xjt.blog.service.TBlogService;
import org.xjt.blog.utils.RespBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/blog")
public class TBlogController {
    @Autowired
    private TBlogService tBlogService;

    /*
    * -------------------------------------------------------------->前台请求
    * */
    //轮播数据
    @GetMapping("front/carousel")
    @ApiOperation("博客列表轮播数据查询")
    public RespBean getCarouselListData(){
        IPage<TBlog> blogsByCommentsViews = tBlogService.getBlogsByCommentsViews();

        return RespBean.ok("ok",blogsByCommentsViews.getRecords());
    }

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
        log.debug("current="+current);
        log.debug("size="+size);
        IPage<TBlog> blogsByPage = tBlogService.getBlogsByPage(current, size, published, flag, share_statement, is_delete);

        return RespBean.ok(blogsByPage);
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

        List<Map<String, String>> blogsByPageHelper = tBlogService.getBlogsByPageHelper(current, size, type_id, published, flag, share_statement, is_delete);
        if(ObjectUtils.isEmpty(blogsByPageHelper)){
            return RespBean.error("没有找到任何博客");
        }else{
            return RespBean.ok("ok",blogsByPageHelper);
        }
    }

    //聚合数据-历史上的今天
    @GetMapping("/front/juhe/todayevent")
    public RespBean juheTodayHistoryEvent(){
        Map map = tBlogService.todayHistoryEvent();

        return RespBean.ok("ok",map);
    }

    //通过id查询博客详情
    @GetMapping("/detail")
    @ApiOperation("前台-通过id查询博客详情")
    public RespBean getBlogDetailById(@RequestParam("blog_id") String blog_id){
        log.warn("blog_id=="+blog_id);
        TBlog blogDetailById = tBlogService.getBlogDetailById(blog_id);

        return RespBean.ok("ok",blogDetailById);
    }

    /*
    * -------------------------------------------------------------->后台请求
    * */
    //查博客总数量
    @GetMapping("/all/counts")
    public RespBean getBlogAllCounts(){
        int count = tBlogService.getBlogAllCounts();
        if (count <= 0) {
            return RespBean.error("查询失败...");
        } else {
            return RespBean.ok("ok",count);
        }
    }

    //根据所有博客title获取词云图
    @GetMapping("/all/wordcloud")
    public RespBean getAllBlogTitleToWordCloud(){
        List<Map<String, Object>> allBlogTitleToWordCloud = tBlogService.getAllBlogTitleToWordCloud();

        return RespBean.ok("ok",allBlogTitleToWordCloud);
    }

    //根据博客标题模糊查询
    @GetMapping("/findByLikeTitle")
    @ApiOperation("通过博客标题查询")
    @ApiImplicitParam(name = "title",value = "博客文章的标题")
    public RespBean findBlogByTitle(@RequestParam("title") String title){
        IPage<TBlog> blogsByTitle = tBlogService.getBlogByTitle(title);

        if (ObjectUtils.isEmpty(blogsByTitle.getRecords())) {

            return RespBean.warn("没有找到类似title的文章");
        } else {
            return RespBean.ok(blogsByTitle.getRecords());
        }
    }

    //保存文章和标签
    @PostMapping("/save")
    @ApiOperation("通过博客标题查询")
    @ApiImplicitParam(name = "title",value = "博客文章的标题")
    public RespBean saveBlogTag(@RequestBody HashMap<String,Object> params){
        TBlog tBlog = tBlogService.saveBlog(params);

        if(tBlog == null){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok",tBlog);
        }
    }

    //更新博客
    @PostMapping("/update")
    @ApiOperation("通过博客bid更新")
    @ApiImplicitParam(name = "bid",value = "通过博客bid更新")
    public RespBean updateBlogTag(@RequestBody HashMap<String, Object> params){
        System.out.println(params);
        TBlog tBlog = tBlogService.updateBlog(params);

        if (ObjectUtils.isEmpty(tBlog)) {
            return RespBean.error("修改博客失败...");
        } else {
            return RespBean.ok("成功修改博客",tBlog);
        }
    }

    //通过id查询
    @GetMapping("getById")
    @ApiOperation("通过bid查询")
    @ApiImplicitParam(name = "bid",value = "通过bid查询")
    public RespBean getBlogById(@RequestParam("bid") String bid){
        Object obj = tBlogService.getBlogById(bid);

        return RespBean.ok(JSON.toJSON(obj));
    }

    //通过id删除
    @DeleteMapping("/delete")
    @ApiOperation("通过id删除博客")
    @ApiImplicitParam(name = "id",value = "通过id删除博客")
    public RespBean deleteBlogById(@RequestParam("bid") String bid){
        int i = tBlogService.deleteBlogById(bid);
        if (i > 0) {
            return RespBean.ok("成功删除博客");
        } else {
            return RespBean.error("删除博客失败...");
        }
    }

    //type对应的blog数量
    @GetMapping("/counts/byType")
    public RespBean getBlogCountsByType(){
        List<Map<String, Integer>> blogCountsByType = tBlogService.getBlogCountsByType();

        return RespBean.ok("ok",blogCountsByType);
    }

}
