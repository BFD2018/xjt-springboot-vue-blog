package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xjt.blog.entity.TType;

@Mapper
public interface TTypeMapper extends BaseMapper<TType> {
}
