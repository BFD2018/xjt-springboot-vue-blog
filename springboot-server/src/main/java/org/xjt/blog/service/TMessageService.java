package org.xjt.blog.service;

import org.xjt.blog.entity.TMessage;
import org.xjt.blog.utils.RespBean;

/**
 * @author xiong
 * @ClassName TMessageService.java
 * @createTime 2021/12/9
 * @Description TODO
 */
public interface TMessageService {
    RespBean getAllMessageByUid(String uid);

    RespBean addMessage(TMessage tMessage);

    RespBean deleteMessageById(Long id);

    RespBean backGetAllMessagesNum();
}
