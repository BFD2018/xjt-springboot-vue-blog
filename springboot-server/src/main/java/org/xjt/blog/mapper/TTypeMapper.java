package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xjt.blog.entity.TType;

@Repository
@Transactional
public interface TTypeMapper extends BaseMapper<TType> {
}
