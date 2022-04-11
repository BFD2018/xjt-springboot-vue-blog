package org.xjt.blog.service;

import org.xjt.blog.entity.TComment;
import org.xjt.blog.utils.RespBean;

/**
 * @author xiong
 * @ClassName TCommentService.java
 * @createTime 2021/11/26
 * @Description TODO
 */
public interface TCommentService {
    RespBean save(TComment tComment);

    RespBean deleteCommentById(Long id);

    RespBean findCommentById(Long id);

    RespBean getAllCommentByBlogId(String blog_id);

    RespBean getAllComments();

    RespBean backGetCommentNumGroupByUserId();
}
