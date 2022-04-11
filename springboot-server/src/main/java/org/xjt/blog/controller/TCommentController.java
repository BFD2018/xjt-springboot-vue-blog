package org.xjt.blog.controller;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xjt.blog.entity.TComment;
import org.xjt.blog.service.TCommentService;
import org.xjt.blog.utils.RespBean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class TCommentController {
    @Autowired
    private TCommentService tCommentService;

    @PostMapping("/save")
    public RespBean save(@RequestBody HashMap<String,String> params) {
        System.out.println("params===================>");
        System.out.println(params);

        String user_id = params.get("user_id");
        String content = params.get("content");
        String blog_id = params.get("blog_id");
        String parent_comment_id = params.get("parent_comment_id");
        TComment tComment = new TComment().setUserId(Long.valueOf(user_id))
                .setContent(content)
                .setBlogId(Long.valueOf(blog_id))
                .setParentCommentId(Long.valueOf(parent_comment_id))
                .setCreateTime(DateUtil.toLocalDateTime(new Date()));

        return tCommentService.save(tComment);
    }

    @GetMapping("/delete")
    public RespBean delete(@RequestParam("id") Long id) {
        return tCommentService.deleteCommentById(id);
    }

    /*通过id查找comment*/
    @GetMapping("/getById")
    public RespBean findById(@RequestParam("id") Long id) {
        return tCommentService.findCommentById(id);
    }

    /*通过blog_id查找对该博客的所有评论*/
    @GetMapping("/getAllByBlogId")
    public RespBean getAllCommentByBlogId(@RequestParam("blog_id") String blog_id) {
        return tCommentService.getAllCommentByBlogId(blog_id);
    }

    /*后端*/
    //获取所有评论
    @GetMapping("/getAll")
    public RespBean getAllComments() {
        return tCommentService.getAllComments();
    }

    //按用户id分组评论数量
    @GetMapping("/back/allByUser")
    public RespBean backGetCommentNumGroupByUserId() {
        return tCommentService.backGetCommentNumGroupByUserId();
    }

}
