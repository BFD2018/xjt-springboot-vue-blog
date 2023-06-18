package org.xjt.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.xjt.blog.entity.TBlog;
import org.xjt.blog.vo.BlogUserVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TBlogService {
    IPage<TBlog> getBlogsByPage(Integer current, Integer size, Boolean published, String flag, String typeId,Boolean share_statement, Boolean is_delete);

    List<Map<String,String>> getBlogsByPageHelper(int current, int size, String type_id, Boolean published, String flag, Boolean share_statement, Boolean is_delete);

    IPage<TBlog> getBlogByTitle(String title);

    TBlog saveBlog(HashMap<String, Object> params);

    Object getBlogById(String bid);

    int deleteBlogById(String bid);

    TBlog updateBlog(HashMap<String, Object> params);

    int getBlogAllCounts();

    BlogUserVo getBlogDetailById(String bid);

    List<Map<String, Integer>> getBlogCountsByType();

    List<Map<String, Object>> getAllBlogTitleToWordCloud();

    Map todayHistoryEvent();

    IPage<TBlog> getBlogsByCommentsViews();
}
