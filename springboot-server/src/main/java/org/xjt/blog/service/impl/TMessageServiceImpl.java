package org.xjt.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xjt.blog.entity.TMessage;
import org.xjt.blog.mapper.TMessageMapper;
import org.xjt.blog.service.TMessageService;
import org.xjt.blog.utils.RespBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TMessageServiceImpl implements TMessageService {
    @Autowired
    private TMessageMapper tMessageMapper;

    @Override
    public RespBean addMessage(TMessage tMessage) {
        Integer ret = tMessageMapper.insert(tMessage);

        if(ret<0){
            return RespBean.error("评论失败");
        }else{
            return RespBean.ok("评论成功",tMessage);
        }
    }

    @Override
    public RespBean deleteMessageById(Long id) {
        int i = tMessageMapper.deleteById(id);
        if(i<0){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok");
        }
    }

    @Override
    public RespBean backGetAllMessagesNum() {
        int count = tMessageMapper.selectCount(null);
        if(count<0){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok",count);
        }

    }

    //存放迭代找出的所有子代回复的集合
    private List<Map<String, Object>> tempReplys = new ArrayList<>();

    @Override
    public RespBean getAllMessageByUid(String uid) {
        //1、查找所有父节点（一级留言）
        List<Map<String, Object>> level1MessageList = tMessageMapper.selectLevel1Messages(uid,String.valueOf("-1"));

        for (Map<String, Object> level1Message : level1MessageList) {
            //一级留言的id
            String id = String.valueOf(level1Message.get("id"));
            String nickname1 = String.valueOf(level1Message.get("nickname"));

            //一级回复（对留言的直接回复）
            List<Map<String, Object>> levelOneReplysList = tMessageMapper.selectLevel1Replys(uid,id);

            if(levelOneReplysList.size() > 0){
                //循环找出子留言的id
                for(Map<String, Object> levelOneReply : levelOneReplysList){
                    levelOneReply.put("parent_comment_nickname",nickname1);
                    String nickname2 = String.valueOf(levelOneReply.get("nickname"));
                    tempReplys.add(levelOneReply);
                    String childId = String.valueOf(levelOneReply.get("id"));

                    //查询出子二级评论
                    recursively(uid, childId,nickname2);
                }
            }

            level1Message.put("replyComments",tempReplys);
            tempReplys = new ArrayList<>();
        }

        return RespBean.ok("ok",level1MessageList);
    }

    private void recursively(String uid, String childId,String parentNickname) {
        //根据子一级回复的id找到子二级回复
        List<Map<String, Object>> replayComments = tMessageMapper.selectLevel2Replys(uid,childId);

        if(replayComments.size() > 0){
            for(Map<String, Object> replayComment : replayComments){
                String replayId = String.valueOf(replayComment.get("id"));
                replayComment.put("parent_comment_nickname",parentNickname);
                String nickname = String.valueOf(replayComment.get("nickname"));
                tempReplys.add(replayComment);

                recursively(uid,replayId,nickname);
            }
        }
    }
}
