package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.xjt.blog.entity.TUser;

@Repository
public interface TUserMapper extends BaseMapper<TUser> {
    TUser selectRolesByUsername(String principal);
}
