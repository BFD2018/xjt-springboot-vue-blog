package org.xjt.blog.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xjt.blog.entity.TType;
import org.xjt.blog.service.TTypeService;
import org.xjt.blog.utils.RespBean;

import java.util.HashMap;

@RequestMapping("/type")
@RestController
public class TTypeController {
    @Autowired
    private TTypeService tTypeService;

    RespBean resp = RespBean.build();

    /*保存分类专栏*/
    @PostMapping("/save")
    private RespBean toSaveType(@RequestBody HashMap<String,String> params){
        //1、判断前端提交的分类名称不能为空（也可以放在前端做）
//        if(!StringUtils.hasLength(typeName)){
//            return RespBean.error("分类专栏名称不能为空！");
//        }
        //2、判断该分类名称是否已存在（也可以放在前端做）
//        RespBean respBean = this.toGetTypeByName(typeName);
//        if(respBean.getStatus() == 200){
//            return RespBean.error("添加失败，已有该分类专栏名称");
//        }

        String typeName = params.get("typeName");

        return tTypeService.saveType(typeName);
    }

    /*查询所有分类*/
    @GetMapping("/all")
    private RespBean getAllType(){
        return tTypeService.getAllType();
    }

    /*分类的分页查询*/
    @GetMapping("/getByPage/{currentPage}/pageSize")
    @ApiOperation("分类的分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示个数")
    })
    private RespBean toGetTypeByPage(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize){
        return tTypeService.getTypeByPage(currentPage,pageSize);
    }

    /*根据id查询分类*/
    @GetMapping("/getById/{id}")
    @ApiOperation("通过id查询分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "分类id"),
    })
    private RespBean toGetTypeById(@PathVariable("id") String id){
        return tTypeService.getTypeById(id);
    }

    /*通过名称查找分类*/
    @GetMapping("/getByName/{name}")
    @ApiOperation("通过name查询分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "分类name"),
    })
    private RespBean toGetTypeByName(@PathVariable("name") String name){
        return tTypeService.getTypeByName(name);
    }


    /*更新分类byID*/
    @PostMapping("/update")
    @ApiOperation("通过id更新分类")
    private RespBean toUpdateType(@RequestBody TType tType){
        return tTypeService.updateType(tType);
    }

    /*删除分类byID*/
    @PutMapping("/delete/{id}")
    @ApiOperation("通过id删除分类tag")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "更新分类tag"),
    })
    private RespBean toDeleteType(@PathVariable("id") String id){
        return tTypeService.deleteType(id);
    }

}
