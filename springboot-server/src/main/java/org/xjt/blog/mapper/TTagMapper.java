package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.xjt.blog.entity.TTag;

@Repository
public interface TTagMapper extends BaseMapper<TTag> {
    Integer selectTagToBlogNum(Integer tag_id);
}
