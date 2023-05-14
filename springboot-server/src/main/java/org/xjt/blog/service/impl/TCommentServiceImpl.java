package org.xjt.blog.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.entity.TComment;
import org.xjt.blog.mapper.TCommentMapper;
import org.xjt.blog.service.TCommentService;
import org.xjt.blog.utils.RedisUtils;
import org.xjt.blog.utils.RespBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TCommentServiceImpl implements TCommentService {
    @Autowired
    private TCommentMapper tCommentMapper;

    @Autowired
    private RedisUtils redisUtils;

    //存放迭代找出的所有子代回复的集合
    private List<Map<String, Object>> tempReplys = new ArrayList<>();

    @Override
    public RespBean save(TComment tComment) {
        Integer ret = tCommentMapper.insert(tComment);

        if(ret<0){
            return RespBean.error("评论失败");
        }else{
            return RespBean.ok("评论成功",tComment);
        }
    }

    @Override
    public RespBean deleteCommentById(Long id) {
        int i = tCommentMapper.deleteById(id);
        if(i<0){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok");
        }
    }

    @Override
    public RespBean findCommentById(Long id) {
        TComment tComment = tCommentMapper.selectById(id);
        if(ObjectUtils.isEmpty(tComment)){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok",tComment);
        }
    }

    @Override
    public RespBean getAllCommentByBlogId(String blog_id) {
        //1、查找所有父节点（对博客的直接评论）
        List<Map<String, Object>> levelOneCommentList = tCommentMapper.selectLevelOneComments(blog_id,String.valueOf("-1"));

        for (Map<String, Object> levelOneComment : levelOneCommentList) {
            //一级评论的id
            String id = String.valueOf(levelOneComment.get("id"));
            String nickname1 = String.valueOf(levelOneComment.get("nickname"));

            //一级回复（对评论的直接回复）
            List<Map<String, Object>> levelOneReplysList = tCommentMapper.selectLevelOneReplys(blog_id,id);

            if(levelOneReplysList.size() > 0){
                //循环找出子评论的id
                for(Map<String, Object> levelOneReply : levelOneReplysList){
                    levelOneReply.put("parent_comment_nickname",nickname1);
                    String nickname2 = String.valueOf(levelOneReply.get("nickname"));
                    tempReplys.add(levelOneReply);
                    String childId = String.valueOf(levelOneReply.get("id"));

                    //查询出子二级评论
                    recursively(blog_id, childId,nickname2);
                }
            }

            levelOneComment.put("replyComments",tempReplys);
            tempReplys = new ArrayList<>();
        }

        return RespBean.ok("ok",levelOneCommentList);
    }

    private void recursively(String blogId, String childId,String parentNickname) {
        //根据子一级回复的id找到子二级回复
        List<Map<String, Object>> replayComments = tCommentMapper.selectLevelTwoReplys(blogId,childId);

        if(replayComments.size() > 0){
            for(Map<String, Object> replayComment : replayComments){
                String replayId = String.valueOf(replayComment.get("id"));
                replayComment.put("parent_comment_nickname",parentNickname);
                String nickname = String.valueOf(replayComment.get("nickname"));
                tempReplys.add(replayComment);

                recursively(blogId,replayId,nickname);
            }
        }
    }

    @Override
    public RespBean getAllComments() {
        boolean exists = redisUtils.exists("blog-blogsCountByType");
        List<Map<String,String>> commentList = null;

        if(!exists || ObjectUtils.isEmpty(redisUtils.get("allComments"))){
            System.out.println("--------->从数据库中获取数据");
            commentList = tCommentMapper.getAllComments();
            redisUtils.set("blog-allComments",commentList);
        }else{
            System.out.println("============>从redis中获取数据");
            commentList = (List<Map<String,String>>)redisUtils.get("allComments");
        }

        if(ObjectUtils.isEmpty(commentList)){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok",commentList);
        }
    }

    @Override
    public RespBean backGetCommentNumGroupByUserId() {
        List<Map<String,String>> commentList = tCommentMapper.getCommentNumGroupByUserId();

        if(ObjectUtils.isEmpty(commentList)){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok",commentList);
        }
    }




}
