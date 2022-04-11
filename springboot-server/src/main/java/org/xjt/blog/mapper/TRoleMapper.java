package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.xjt.blog.entity.TRole;

import java.util.List;
import java.util.Map;

@Repository
public interface TRoleMapper extends BaseMapper<TRole> {
    List<Map<String, Integer>> getUserCountsGroupByRole();
}
