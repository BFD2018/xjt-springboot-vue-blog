package org.xjt.blog.service;


import org.xjt.blog.entity.TBlog;
import org.xjt.blog.utils.RespBean;

import java.util.HashMap;

public interface TBlogService {
    RespBean getBlogsByPage(Integer current, Integer size, Boolean published, String flag, Boolean share_statement, Boolean is_delete);

    RespBean getBlogsByPageHelper(Integer current, Integer size,String type_id, Boolean published, String flag, Boolean share_statement, Boolean is_delete);

    RespBean getBlogByTitle(String title);

    RespBean saveBlog(HashMap<String, Object> params);

    RespBean getBlogById(String bid);

    RespBean deleteBlogById(String bid);

    RespBean updateBlog(HashMap<String, Object> params);

    RespBean getBlogAllCounts();

    RespBean getBlogDetailById(String bid);

    RespBean getBlogCountsByType();

    RespBean getAllBlogTitleToWordCloud();
}
