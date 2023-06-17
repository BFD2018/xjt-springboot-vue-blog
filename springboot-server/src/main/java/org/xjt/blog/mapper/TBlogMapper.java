package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;
import org.xjt.blog.entity.TBlog;

import java.util.List;
import java.util.Map;


@Transactional
public interface TBlogMapper extends BaseMapper<TBlog> {
    Object findBlogById(String bid);

    List<Map<String,String>> getBlogsByPageHelper(int current,int size,String type_id, Boolean published, String flag, Boolean share_statement, Boolean is_delete);

    Map<String,String> findBlogDetailById(String bid);

    List<Map<String, Integer>> getBlogCountsGroupByType();
}
