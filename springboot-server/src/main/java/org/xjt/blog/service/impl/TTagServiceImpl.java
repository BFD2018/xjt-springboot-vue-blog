package org.xjt.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.entity.TTag;
import org.xjt.blog.mapper.TTagMapper;
import org.xjt.blog.service.TTagService;
import org.xjt.blog.utils.RespBean;

import java.util.*;

@Service
public class TTagServiceImpl implements TTagService {
    @Autowired
    private TTagMapper tTagMapper;

    @Override
    public RespBean selectTagByLikeName(String name) {
        QueryWrapper<TTag> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);
        List<TTag> tagList = tTagMapper.selectList(wrapper);

        if(ObjectUtils.isEmpty(tagList)){
            return RespBean.error("未找到相似tag");
        }else{
            return RespBean.ok(tagList);
        }
    }

    @Override
    public TTag selectTagByName(String name) {
        QueryWrapper<TTag> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        TTag tTag = tTagMapper.selectOne(wrapper);
        return tTag;
    }

    @Override
    public RespBean saveTag(String name) {
        TTag tTag = new TTag().setName(name);
        int insert = tTagMapper.insert(tTag);

        if(insert<0){
            return RespBean.error("新建tag失败...");
        }else{
            return RespBean.ok("新建tag成功");
        }
    }

    @Override
    public RespBean getAllTag() {
        List<TTag> tTags = tTagMapper.selectList(null);
        if(ObjectUtils.isEmpty(tTags)){
            return RespBean.error("数据库中还没有tag，快去创建一个吧");

        }else{
            return RespBean.ok(tTags);
        }

    }

    @Override
    public RespBean updateTag(TTag tag) {
        int update = tTagMapper.updateById(tag);
        if(update>0){
            return RespBean.ok("tag更新成功");
        }else{
            return RespBean.error("tag更新失败...");
        }
    }

    @Override
    public RespBean deleteTag(String tid) {
        int i = tTagMapper.deleteById(tid);
        if(i>0){
            return RespBean.ok("tag删除成功");
        }else{
            return RespBean.error("tag删除失败...");
        }

    }

    @Override
    public RespBean getTagByPage(Integer current, Integer pageSize) {
        Page<TTag> tagPage = new Page<>(current,pageSize);
        IPage<TTag> tTagIPage = tTagMapper.selectPage(tagPage, null);

        List<TTag> records = tTagIPage.getRecords();
        if(ObjectUtils.isEmpty(records)){
            return RespBean.warn("数据库中没有tag");
        }else{
            return RespBean.ok(tagPage);
        }
    }

    @Override
    public RespBean getAllTagToBlogNum() {
        List<HashMap<String, Object>> mapList = new ArrayList<>();

        List<TTag> tTags = tTagMapper.selectList(null);
        for(Integer i = 0; i < tTags.size(); i++){
            Integer num = tTagMapper.selectTagToBlogNum(tTags.get(i).getId());
            String tag_name = tTags.get(i).getName();
            String tag_color = tTags.get(i).getColor();
            HashMap<String, Object> map = new HashMap<>();
            map.put("tag_name",tag_name);
            map.put("tag_color",tag_color);
            map.put("blog_num",num);
            System.out.println(map);
            mapList.add(map);
        }

        if(ObjectUtils.isEmpty(mapList)){
            return RespBean.error("error");
        }else{
            return RespBean.ok("ok",mapList);
        }
    }
}
