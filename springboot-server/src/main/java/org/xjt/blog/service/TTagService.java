package org.xjt.blog.service;

import org.xjt.blog.entity.TTag;
import org.xjt.blog.utils.RespBean;

import java.util.List;

/**
 * @author xiong
 * @ClassName TTagService.java
 * @createTime 2021/11/2
 * @Description TODO
 */
public interface TTagService {
    RespBean selectTagByLikeName(String name);

    TTag selectTagByName(String name);

    RespBean saveTag(String name);

    RespBean getAllTag();

    RespBean updateTag(TTag tag);

    RespBean deleteTag(String tid);

    RespBean getTagByPage(Integer current, Integer pageSize);

    RespBean getAllTagToBlogNum();
}
