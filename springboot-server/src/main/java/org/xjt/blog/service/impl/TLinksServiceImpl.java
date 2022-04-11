package org.xjt.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.entity.TLinks;
import org.xjt.blog.entity.TTag;
import org.xjt.blog.mapper.TLinksMapper;
import org.xjt.blog.service.TLinksService;
import org.xjt.blog.utils.RespBean;

import java.util.List;

@Service
public class TLinksServiceImpl implements TLinksService {
    @Autowired
    private TLinksMapper tLinksMapper;

    @Override
    public List<TLinks> selectLinkByBlogName(String blogName) {
        List<TLinks> tLinksList = tLinksMapper.selectList(new QueryWrapper<TLinks>().eq("blog_name", blogName));
        return tLinksList;
    }

    @Override
    public RespBean save(TLinks link) {
        int insert = tLinksMapper.insert(link);
        if(insert>0){
            return RespBean.ok("添加link成功",link);
        }else{
            return RespBean.error("link添加失败...");
        }
    }

    @Override
    public RespBean selectLinkByLikeName(String name) {
        QueryWrapper<TLinks> wrapper = new QueryWrapper<>();
        wrapper.like("blog_name",name);
        List<TLinks> linksList = tLinksMapper.selectList(wrapper);

        if(ObjectUtils.isEmpty(linksList)){
            return RespBean.error("未找到相似tag");
        }else{
            return RespBean.ok(linksList);
        }
    }

    @Override
    public RespBean getAllLink() {
        QueryWrapper<TLinks> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_time");
        List<TLinks> linksList = tLinksMapper.selectList(wrapper);
        if(ObjectUtils.isEmpty(linksList)){
            return RespBean.warn("数据库中还没有tag，快去创建一个吧");

        }else{
            return RespBean.ok(linksList);
        }
    }

    @Override
    public RespBean getLinkByPage(Integer current, Integer pageSize) {
        Page<TLinks> page = new Page<>(current,pageSize);

        QueryWrapper<TLinks> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_time");

        tLinksMapper.selectPage(page, wrapper);

        List<TLinks> records = page.getRecords();
        if(ObjectUtils.isEmpty(records)){
            return RespBean.warn("数据库中没有link");
        }else{
            return RespBean.ok("ok",page);
        }
    }

    @Override
    public RespBean updateLink(TLinks link) {
        int update = tLinksMapper.updateById(link);
        if(update>0){
            return RespBean.ok("link更新成功");
        }else{
            return RespBean.error("link更新失败...");
        }
    }

    @Override
    public RespBean deleteLink(String lid) {
        int i = tLinksMapper.deleteById(lid);
        if(i>0){
            return RespBean.ok("link删除成功");
        }else{
            return RespBean.error("link删除失败...");
        }
    }
}
