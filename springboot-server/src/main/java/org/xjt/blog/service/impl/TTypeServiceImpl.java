package org.xjt.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xjt.blog.entity.TType;
import org.xjt.blog.mapper.TTypeMapper;
import org.xjt.blog.service.TTypeService;

import java.util.List;

@Slf4j
@Service
public class TTypeServiceImpl implements TTypeService {
    @Autowired
    private TTypeMapper tTypeMapper;

    @Override
    public int saveType(String typeName) {
        TType tType = new TType().setName(typeName);
        int insert = tTypeMapper.insert(tType);

        return insert;
    }

    @Override
    public List<TType> queryAllTypeList() {
        List<TType> tTypeList = tTypeMapper.selectList(null);

        return tTypeList;
    }

    @Override
    public IPage<TType> getTypeByPage(Integer currentPage, Integer pageSize) {
        Page<TType> pager = new Page<>(currentPage, pageSize);
        IPage<TType> typesIPage = tTypeMapper.selectPage(pager, null);

        return typesIPage;
    }

    @Override
    public TType getTypeById(String id) {
        TType tType = tTypeMapper.selectById(id);

        return tType;
    }

    @Override
    public List<TType> getTypeByName(String name) {
        QueryWrapper<TType> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);
        List<TType> typeList = tTypeMapper.selectList(wrapper);

        return typeList;


    }

    @Override
    public int updateType(TType tType) {
        int update = tTypeMapper.updateById(tType);

        return update;

    }

    @Override
    public int deleteType(String id) {
        int i = tTypeMapper.deleteById(id);

        return i;
    }
}
