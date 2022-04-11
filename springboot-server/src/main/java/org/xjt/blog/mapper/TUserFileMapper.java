package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.xjt.blog.entity.TUserFile;

import java.util.List;
import java.util.Map;

@Repository
public interface TUserFileMapper extends BaseMapper<TUserFile> {
    List<Map<String, Object>> getCountsGroupByUserAndFileType();
}
