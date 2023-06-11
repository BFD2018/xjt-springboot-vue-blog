package org.xjt.blog.service;

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
    RespBean saveType(String typeName);

    List<TType> getAllType();

    RespBean getTypeByPage(Integer currentPage, Integer pageSize);

    RespBean getTypeById(String id);

    RespBean getTypeByName(String name);

    RespBean updateType(TType tType);

    RespBean deleteType(String id);

}
