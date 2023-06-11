package org.xjt.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.xjt.blog.entity.TType;
import org.xjt.blog.mapper.TTypeMapper;
import org.xjt.blog.service.TTypeService;
import org.xjt.blog.utils.RespBean;

import java.util.List;

@Slf4j
@Service
public class TTypeServiceImpl implements TTypeService {
    @Autowired
    private TTypeMapper tTypeMapper;

    @Override
    public RespBean saveType(String typeName) {
        TType tType = new TType().setName(typeName);
        int insert = tTypeMapper.insert(tType);

        if(insert>0){
            return RespBean.ok("分类专栏 添加成功");
        }else{
            return RespBean.error("分类专栏 添加失败");
        }
    }

    @Cacheable
    @Override
    public List<TType> getAllType() {
        List<TType> tTypeList = tTypeMapper.selectList(null);

        return tTypeList;
    }

    @Override
    public RespBean getTypeByPage(Integer currentPage, Integer pageSize) {
        Page<TType> pager = new Page<>(currentPage, pageSize);
        IPage<TType> tTypeIPage = tTypeMapper.selectPage(pager, null);
        List<TType> records = tTypeIPage.getRecords();
        long total = tTypeIPage.getTotal();
        long size = tTypeIPage.getSize();
        long pages = tTypeIPage.getPages();

        return RespBean.ok(tTypeIPage);
    }

    @Override
    public RespBean getTypeById(String id) {
        TType tType = tTypeMapper.selectById(id);

        return RespBean.ok(tType);
    }

    @Override
    public RespBean getTypeByName(String name) {
        QueryWrapper<TType> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);
        List<TType> typeList = tTypeMapper.selectList(wrapper);
        if (ObjectUtils.isEmpty(typeList)) {
            return RespBean.error("error");

        }else{
            return RespBean.ok("ok",typeList);
        }


    }

    @Override
    public RespBean updateType(TType tType) {
        int update = tTypeMapper.updateById(tType);
        if(update>0){
            return RespBean.ok("修改Blog分类名称成功");
        }else{
            return RespBean.error("修改Blog分类名称失败...");
        }

    }

    @Override
    public RespBean deleteType(String id) {
        int i = tTypeMapper.deleteById(id);

        if(i>0){
            return RespBean.ok("删除Blog分类Tag成功");
        }else{
            return RespBean.error("修改Blog分类Tag失败...");
        }
    }
}
