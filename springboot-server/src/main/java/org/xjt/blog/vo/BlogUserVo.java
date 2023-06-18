package org.xjt.blog.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BlogUserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "博客id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "首图地址")
    private String firstPicture;

    @ApiModelProperty(value = "博客内容")
    private String content;

    @ApiModelProperty(value = "原创")
    private String flag;

    @ApiModelProperty(value = "是否公开")
    private Boolean published;

    @ApiModelProperty(value = "是否开启赞赏")
    private Boolean appreciation;

    @ApiModelProperty(value = "是否开启评论")
    private Boolean commentabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/ShangHai")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否开启回复")
    private Boolean recommend;

    @ApiModelProperty(value = "是否已发布-用于判断blog是草稿还是已发布")
    private Boolean shareStatement;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/ShangHai")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @JsonSerialize(using = ToStringSerializer.class) //系统序列化时，保留相关精度
    @ApiModelProperty(value = "分类id")
    private Long typeId;

    @JsonSerialize(using = ToStringSerializer.class) //系统序列化时，保留相关精度
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class) //系统序列化时，保留相关精度
    @ApiModelProperty(value = "评论次数")
    private Long commentCount;

    @ApiModelProperty(value = "逻辑删除")
    private Boolean isDelete;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;
}

