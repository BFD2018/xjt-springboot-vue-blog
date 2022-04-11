package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.xjt.blog.entity.TComment;

import java.util.List;
import java.util.Map;

@Repository
public interface TCommentMapper extends BaseMapper<TComment> {
    //返回 detailCommentList
    List<Map<String, Object>> selectLevelOneComments(String blog_id,String parent_id);

    List<Map<String, Object>> selectLevelOneReplys(String blog_id, String parent_id);

    List<Map<String, Object>> selectLevelTwoReplys(String blog_id, String parent_id);

    List<Map<String, String>> getAllComments();

    List<Map<String, String>> getCommentNumGroupByUserId();
}
