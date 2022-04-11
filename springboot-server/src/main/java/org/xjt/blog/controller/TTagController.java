package org.xjt.blog.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.xjt.blog.entity.TTag;
import org.xjt.blog.service.TTagService;
import org.xjt.blog.utils.RespBean;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TTagController {
    @Autowired
    private TTagService tTagService;

    @PostMapping("/save")
    public RespBean saveTag(@RequestBody HashMap<String,String> params){
        String tagName = params.get("tagName");

        System.out.println("tagName================>");
        System.out.println(tagName);

        return tTagService.saveTag(tagName);
    }

    /*按照tag名称模糊查询*/
    @GetMapping("/findByName/like")
    public RespBean findTagByLikeName(@RequestParam("name") String name){
        return tTagService.selectTagByLikeName(name);
    }

    /*查找所有tag*/
    @GetMapping("/all")
    public RespBean getAllTag(){
        return tTagService.getAllTag();
    }

    /*查找所有tag及其对应的Blog数量*/
    @GetMapping("/allTagToBlogNum")
    public RespBean getAllTagToBlogNum(){
        return tTagService.getAllTagToBlogNum();
    }

    /*分页查找tag*/
    @GetMapping("/getByPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "当前页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量"),
    })
    public RespBean getTagByPage(@RequestParam(value = "current",required = false,defaultValue = "1") Integer current,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "6") Integer pageSize){
        return tTagService.getTagByPage(current,pageSize);
    }

    /*更新tag*/
    @PostMapping("/update")
    public RespBean updateTag(@RequestBody TTag tag){
        System.out.println(tag);
        return tTagService.updateTag(tag);
    }

    /*删除tag*/
    @DeleteMapping("/delete")
    public RespBean deleteTag(@RequestParam("tid") String tid){
        return  tTagService.deleteTag(tid);
    }
}
