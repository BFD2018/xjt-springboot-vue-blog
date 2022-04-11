package org.xjt.blog.service;

import org.xjt.blog.entity.TLinks;
import org.xjt.blog.utils.RespBean;

import java.util.List;

/**
 * @author xiong
 * @ClassName TLinksService.java
 * @createTime 2021/11/3
 * @Description TODO
 */
public interface TLinksService {
    List<TLinks> selectLinkByBlogName(String blogName);

    RespBean save(TLinks link);

    RespBean selectLinkByLikeName(String name);

    RespBean getAllLink();

    RespBean getLinkByPage(Integer current, Integer pageSize);

    RespBean updateLink(TLinks link);

    RespBean deleteLink(String lid);

}
