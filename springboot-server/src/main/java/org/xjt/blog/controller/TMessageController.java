package org.xjt.blog.controller;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xjt.blog.entity.TComment;
import org.xjt.blog.entity.TMessage;
import org.xjt.blog.service.TMessageService;
import org.xjt.blog.utils.RespBean;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/message")
public class TMessageController {
    @Autowired
    private TMessageService tMessageService;

    //获取用户的所有留言
    @GetMapping("/all")
    public RespBean getAllMessageByUid(@RequestParam("id") String id){
        return tMessageService.getAllMessageByUid(id);
    }

    //获取用户的所有留言
    @GetMapping("back/allNum")
    public RespBean backGetAllMessagesNum(){
        return tMessageService.backGetAllMessagesNum();
    }

    //添加留言
    @PostMapping("/add")
    public RespBean addMessage(@RequestBody HashMap<String,String> params) {
        System.out.println("params===================>");
        System.out.println(params);

        //博主id
        String user_id = params.get("user_id");
        //留言内容
        String content = params.get("content");
        //留言者id
        String creator_id = params.get("creator_id");
        String parent_message_id = params.get("parent_comment_id");
        if(!StringUtils.hasText(parent_message_id)){
            parent_message_id = "-1";
        }
        TMessage tMessage = new TMessage().setUserId(Long.valueOf(user_id)).setContent(content)
                .setCreatorId(Long.valueOf(creator_id)).setParentMessageId(Long.valueOf(parent_message_id))
                .setCreateTime(DateUtil.toLocalDateTime(new Date()));


        return tMessageService.addMessage(tMessage);
    }

    @GetMapping("/delete")
    public RespBean delete(@RequestParam("id") Long id) {
        return tMessageService.deleteMessageById(id);
    }
}
