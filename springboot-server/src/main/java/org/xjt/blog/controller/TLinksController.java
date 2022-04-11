package org.xjt.blog.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.xjt.blog.entity.TLinks;
import org.xjt.blog.entity.TTag;
import org.xjt.blog.service.TLinksService;
import org.xjt.blog.service.TTagService;
import org.xjt.blog.utils.RespBean;

import java.util.List;

@RestController
@RequestMapping("/link")
public class TLinksController {
    @Autowired
    private TLinksService tLinksService;

    @PostMapping("/save")
    public RespBean saveLink(@RequestBody TLinks link){
        //1、判断link不能同名
        List<TLinks> tagList = tLinksService.selectLinkByBlogName(link.getBlogName());
        if(!ObjectUtils.isEmpty(tagList)){
            return RespBean.error("同名的Link已存在");
        }

        //2、保存到数据库
        return tLinksService.save(link);
    }

    /*按照link名称模糊查询*/
    @GetMapping("/findByName/like")
    public RespBean findLinkByLikeName(@RequestParam("name") String name){
        return tLinksService.selectLinkByLikeName(name);
    }

    /*查找所有link*/
    @GetMapping("/all")
    public RespBean getAllLink(){
        return tLinksService.getAllLink();
    }

    /*分页查找link*/
    @GetMapping("/getByPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "当前页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量"),
    })
    public RespBean getLinkByPage(@RequestParam(value = "current",required = false,defaultValue = "1") Integer current,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "6") Integer pageSize){
        return tLinksService.getLinkByPage(current,pageSize);
    }

    /*更新link*/
    @PostMapping("/update")
    public RespBean updateLink(@RequestBody TLinks link){
        return tLinksService.updateLink(link);
    }

    /*删除link*/
    @DeleteMapping("/delete")
    public RespBean deleteTag(@RequestParam("lid") String lid){
        return  tLinksService.deleteLink(lid);
    }
}
