package org.xjt.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.common.CacheConstants;
import org.xjt.blog.common.Constants;
import org.xjt.blog.entity.TBlog;
import org.xjt.blog.entity.TBlogTags;
import org.xjt.blog.mapper.TBlogMapper;
import org.xjt.blog.mapper.TBlogTagsMapper;
import org.xjt.blog.service.TBlogService;
import org.xjt.blog.utils.HttpUtils;
import org.xjt.blog.utils.RedisUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@CacheConfig(cacheNames = "blogsevice")
public class TBlogServiceImpl implements TBlogService {
    @Autowired
    private TBlogMapper tBlogMapper;

    @Autowired
    private TBlogTagsMapper tBlogTagsMapper;

    @Autowired
    private RedisUtils redisUtils;




    @Cacheable(value = "blogsByPage",key = "#current")
    @Override
    public IPage<TBlog> getBlogsByPage(Integer current, Integer size, Boolean published, String flag, Boolean share_statement, Boolean is_delete) {
        if(current < 1){
            current  = 1;
        }
        if(size < 1){
            size  = 6;
        }
        Page<TBlog> tBlogPage = new Page<>(current, size);
        QueryWrapper<TBlog> wrapper = new QueryWrapper<>();
        IPage<TBlog> tBlogIPage = null;

        log.debug("------------>从数据库中查询");
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

        return tBlogIPage;

    }


    @Cacheable(value = "getBlogsByPageHelper")
    @Override
    public List<Map<String,String>> getBlogsByPageHelper(int current, int size, String type_id,Boolean published, String flag, Boolean share_statement, Boolean is_delete) {
        List<Map<String,String>> list = tBlogMapper.getBlogsByPageHelper(current,size,type_id,published,flag,share_statement,is_delete);

        return list;
    }

    @Cacheable(value = "getBlogByTitle",key = "#title")
    @Override
    public IPage<TBlog> getBlogByTitle(String title) {
        Page<TBlog> tBlogPage = new Page<TBlog>(Constants.CurrentPage, Constants.PageSize);
        QueryWrapper<TBlog> wrapper = new QueryWrapper<>();
        IPage<TBlog> tBlogIPage = null;

        wrapper.like("title", title);
        wrapper.eq("published", 1);

        tBlogIPage = tBlogMapper.selectPage(tBlogPage, wrapper);


        return tBlogIPage;
    }

    @Transactional
    @CachePut(value = "saveBlog",key = "#params.title")
    @Override
    public TBlog saveBlog(HashMap<String, Object> params) {
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
            return tBlog;
        } else {
            return null;
        }

    }

    @Cacheable(value = "blogById",key = "#bid")
    @Override
    public Object getBlogById(String bid) {
        Object obj = null;

        obj = tBlogMapper.findBlogById(bid);

        if (ObjectUtils.isEmpty(obj)) {
            obj = "没有找到id=" + bid + " 的博客";
        }

        return obj;
    }

    @CacheEvict(value = "blogById",key = "#bid")
    @Override
    public int deleteBlogById(String bid) {
        int i = tBlogMapper.deleteById(bid);

        return i;
    }

    @CachePut(value = "blog",key = "#params.id")
    @Override
    public TBlog updateBlog(HashMap<String, Object> params) {
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
            return null;
        } else {
            return tBlog;
        }
    }

    @Cacheable(value = "allcounts")
    @Override
    public int getBlogAllCounts() {
        int count = tBlogMapper.selectCount(null);

        return count;
    }

    @Cacheable(value = "blogdetail",key = "#bid")
    @Override
    public TBlog getBlogDetailById(String bid) {
        try {
            Object obj = tBlogMapper.findBlogDetailById(bid);
            Map objMap = null;
            if(obj instanceof Map){
                objMap = (Map) obj;
            }

            Integer views = (Integer) objMap.get("views");
            TBlog newBlog = new TBlog().setId(Long.valueOf(bid)).setViews(views+1);
            tBlogMapper.updateById(newBlog);

            if (ObjectUtils.isEmpty(obj)) {
                return null;
            } else {
                return newBlog;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Cacheable(value = "blogCountsByType")
    @Override
    public List<Map<String, Integer>> getBlogCountsByType() {
        List<Map<String, Integer>> ret = null;

        System.out.println("--------->从数据库中获取数据");
        ret = tBlogMapper.getBlogCountsGroupByType();

        return ret;
    }

    @Cacheable(value = "allBlogTitleToWordCloud")
    @Override
    public List<Map<String, Object>> getAllBlogTitleToWordCloud() {
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

        return wordsList;
    }

    @Value("${juhe.todayOnhistory.key}")
    private String todayOnhistoryKey;

    @Cacheable(value = CacheConstants.TODAYHISTORYEVENT)
    @Override
    public Map todayHistoryEvent() {
        Map ret = null;
        String format = DateUtil.format(LocalDateTime.now(), "M/d");

        String url = "http://v.juhe.cn/todayOnhistory/queryEvent.php?key=" + todayOnhistoryKey + "&date=" + format;
        String s = HttpUtils.sendGet(url);
        ret = JSON.parseObject(s, HashMap.class);
        log.warn("todayOnhistory::url=={},map={}",url,ret);

        return ret instanceof Map?(Map)ret:null;
    }

    @Cacheable(value = "carouselListByCommentsViews")
    @Override
    public IPage<TBlog> getBlogsByCommentsViews() {
        Page<TBlog> tBlogPage = new Page<>(1, 4);
        QueryWrapper<TBlog> wrapper = new QueryWrapper<>();

        wrapper.orderByDesc("comment_count","views");
        IPage<TBlog> tBlogIPage = tBlogMapper.selectPage(tBlogPage, wrapper);

        log.info("carouselListByCommentsViews:" + tBlogIPage.getRecords().toArray().toString());

        return tBlogIPage;
    }
}
