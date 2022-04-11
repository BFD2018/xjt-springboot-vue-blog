package org.xjt.blog.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.entity.TBlog;
import org.xjt.blog.entity.TBlogTags;
import org.xjt.blog.mapper.TBlogMapper;
import org.xjt.blog.mapper.TBlogTagsMapper;
import org.xjt.blog.service.TBlogService;
import org.xjt.blog.utils.RedisUtils;
import org.xjt.blog.utils.RespBean;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class TBlogServiceImpl implements TBlogService {
    @Autowired
    private TBlogMapper tBlogMapper;

    @Autowired
    private TBlogTagsMapper tBlogTagsMapper;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public RespBean getBlogsByPage(Integer current, Integer size, Boolean published, String flag, Boolean share_statement, Boolean is_delete) {
        Page<TBlog> tBlogPage = new Page<>(current, size);
        QueryWrapper<TBlog> wrapper = new QueryWrapper<>();
        IPage<TBlog> tBlogIPage = null;

        boolean exists = redisUtils.exists("blog-blogsByPage");

        if(current > 1 || !exists || ObjectUtils.isEmpty(redisUtils.get("blog-blogsByPage"))){
            System.out.println("------------>从数据库中查询");
            if (published != null) {
                wrapper.eq("published", published);
            }
            if (flag != null) {
                wrapper.eq("flag", flag);
            }
            if (share_statement != null) {
                wrapper.eq("share_statement", share_statement);
            }
            if (is_delete != null) {
                wrapper.eq("is_delete", is_delete);
            }
            wrapper.orderByDesc("update_time");

            tBlogIPage = tBlogMapper.selectPage(tBlogPage, wrapper);
            System.out.println(tBlogIPage);

            redisUtils.set("blog-blogsByPage", tBlogIPage);
        }else{
            System.out.println("============>从redis中获取数据");
            tBlogIPage = (IPage<TBlog>)redisUtils.get("blog-blogsByPage");
        }

        return RespBean.ok("ok",tBlogIPage);

    }


    @Override
    public RespBean getBlogsByPageHelper(Integer current, Integer size, String type_id,Boolean published, String flag, Boolean share_statement, Boolean is_delete) {
        PageHelper.startPage(current,size);
        List<Map<String,String>> list = tBlogMapper.getBlogsByPageHelper(type_id,published,flag,share_statement,is_delete);
        if(ObjectUtils.isEmpty(list)){
            return RespBean.error("没有找到任何博客");
        }else{
            PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(list);

            return RespBean.ok("ok",pageInfo);
        }
    }

    @Override
    public RespBean getBlogByTitle(String title) {
        Page<TBlog> tBlogPage = new Page<>();

        QueryWrapper<TBlog> wrapper = new QueryWrapper<>();
        wrapper.like("title", title);
        wrapper.eq("published", 1);

        IPage<TBlog> tBlogIPage = tBlogMapper.selectPage(tBlogPage, wrapper);
        if (ObjectUtils.isEmpty(tBlogIPage.getRecords())) {
            return RespBean.warn("没有找到类似title的文章");
        } else {
            return RespBean.ok(tBlogIPage.getRecords());
        }
    }

    @Override
    public RespBean saveBlog(HashMap<String, Object> params) {
        TBlog tBlog = new TBlog();

        String title = (String) params.get("title");
        String description = (String) params.get("description");
        String first_picture = (String) params.get("first_picture");
        String content = (String) params.get("content");
        String type = (String) params.get("type");
        List tagList = (List) params.get("tags");
        Boolean share_statement = (Boolean) params.get("share_statement");
        Integer published = (Integer) params.get("published");
        Integer flag = (Integer) params.get("flag");

        if (flag == 1) {
            tBlog.setFlag("原创");
        } else {
            tBlog.setFlag("转载");
        }
        if (published == 1) {
            tBlog.setPublished(true);
        } else {
            tBlog.setPublished(false);
        }
        tBlog.setTitle(title);
        tBlog.setDescription(description);
        tBlog.setFirstPicture(first_picture);
        tBlog.setContent(content);
        tBlog.setShareStatement(share_statement);
        tBlog.setTitle(title);
        tBlog.setTypeId(Long.parseLong(type));

        //插入TBlog
        int insert1 = tBlogMapper.insert(tBlog);
        int insert2 = 0;

        if (insert1 > 0 && !ObjectUtils.isEmpty(tagList)) {
            for (Object tag : tagList) {
                long tag_id = Long.parseLong(tag.toString());
                TBlogTags tBlogTags = new TBlogTags();
                tBlogTags.setTagsId(tag_id);
                tBlogTags.setBlogsId(tBlog.getId());

                insert2 = tBlogTagsMapper.insert(tBlogTags);
            }
        }

        if (insert1 > 0 && insert2 > 0) {
            return RespBean.ok("成功创建了一篇博客");
        } else {
            return RespBean.error("创建博客失败...");
        }

    }

    @Override
    public RespBean getBlogById(String bid) {
        try {
            Object obj = tBlogMapper.findBlogById(bid);

            if (ObjectUtils.isEmpty(obj)) {
                return RespBean.error("没有找到id=" + bid + " 的博客");
            } else {
                return RespBean.ok(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    @Override
    public RespBean deleteBlogById(String bid) {
        int i = tBlogMapper.deleteById(bid);

        if (i > 0) {
            return RespBean.ok("成功删除博客");
        } else {
            return RespBean.error("删除博客失败...");
        }
    }

    @Override
    public RespBean updateBlog(HashMap<String, Object> params) {
        TBlog tBlog = new TBlog();

        String id = (String) params.get("id");
        String title = (String) params.get("title");
        String description = (String) params.get("description");
        String first_picture = (String) params.get("first_picture");
        String content = (String) params.get("content");

        Boolean share_statement = (Boolean) params.get("share_statement");
        Integer published = (Integer) params.get("published");
        Integer flag = (Integer) params.get("flag");

        if (flag == 1) {
            tBlog.setFlag("原创");
        } else {
            tBlog.setFlag("转载");
        }
        if (published == 1) {
            tBlog.setPublished(true);
        } else {
            tBlog.setPublished(false);
        }
        tBlog.setId(Long.valueOf(id));
        tBlog.setTitle(title);
        tBlog.setDescription(description);
        tBlog.setFirstPicture(first_picture);
        tBlog.setContent(content);
        tBlog.setShareStatement(share_statement);
        tBlog.setTitle(title);
        tBlog.setUpdateTime(LocalDateTime.now());

        //插入TBlog
        int update = tBlogMapper.updateById(tBlog);
        log.warn("update==="+update);

        if (update < 0) {
            return RespBean.error("修改博客失败...");
        } else {
            return RespBean.ok("成功修改博客");
        }
    }

    @Override
    public RespBean getBlogAllCounts() {
        Integer count = tBlogMapper.selectCount(null);

        if (count <= 0) {
            return RespBean.error("查询失败...");
        } else {
            return RespBean.ok("ok",count);
        }
    }

    @Override
    public RespBean getBlogDetailById(String bid) {
        try {
            Object obj = tBlogMapper.findBlogDetailById(bid);

            Map obj1 = (Map) obj;
            Integer views = (Integer) obj1.get("views");
            TBlog newBlog = new TBlog().setId(Long.valueOf(bid)).setViews(views+1);
            tBlogMapper.updateById(newBlog);

            if (ObjectUtils.isEmpty(obj)) {
                return RespBean.error("没有找到id=" + bid + " 的博客");
            } else {
                return RespBean.ok(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("查询失败");
        }
    }

    @Override
    public RespBean getBlogCountsByType() {
        boolean exists = redisUtils.exists("blog-blogsCountByType");
        List<Map<String, Integer>> ret = null;

        if(!exists || ObjectUtils.isEmpty(redisUtils.get("blog-blogsCountByType"))){
            System.out.println("--------->从数据库中获取数据");
            ret = tBlogMapper.getBlogCountsGroupByType();
            redisUtils.set("blog-blogsCountByType",ret);
        }else{
            System.out.println("============>从redis中获取数据");
            ret = (List<Map<String, Integer>>)redisUtils.get("blog-blogsCountByType");
        }

        if(ObjectUtils.isEmpty(ret)){
            return RespBean.error("查询失败");
        }else{
            return RespBean.ok("ok",ret);
        }
    }

    @Override
    public RespBean getAllBlogTitleToWordCloud() {
        QueryWrapper<TBlog> wrapper = new QueryWrapper<>();
        wrapper.select("title");
        List<TBlog> blogList = tBlogMapper.selectList(wrapper);

        List<Map<String, Object>> wordsList = null;

        for (TBlog blog : blogList) {
            String title = blog.getTitle();

            JiebaSegmenter segmenter = new JiebaSegmenter();
            List<SegToken> tokenList = segmenter.process(title, JiebaSegmenter.SegMode.INDEX);

            tokenList.forEach(item -> {
                String s = item.toString().substring(1, item.toString().length() - 1);
                String name = s.split(",")[0];
                String startOffset = s.split(",")[1].trim();
                String endOffset = s.split(",")[2].trim();
                Integer val = Integer.valueOf(endOffset);

                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("value", val);
                wordsList.add(map);
            });
        }

        return RespBean.ok("ok",wordsList);
    }
}
