package org.xjt.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.xjt.blog.entity.TType;
import org.xjt.blog.utils.RespBean;

import java.util.List;

/**
 * @author xiong
 * @ClassName TTypeService.java
 * @createTime 2021/10/21
 * @Description TODO
 */
public interface TTypeService {
    int saveType(String typeName);

    List<TType> queryAllTypeList();

    IPage<TType> getTypeByPage(Integer currentPage, Integer pageSize);

    TType getTypeById(String id);

    List<TType> getTypeByName(String name);

    int updateType(TType tType);

    int deleteType(String id);

}
