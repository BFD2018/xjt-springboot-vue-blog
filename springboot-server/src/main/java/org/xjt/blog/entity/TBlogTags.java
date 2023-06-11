package org.xjt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="TBlogTags对象", description="")
public class TBlogTags implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "博客id")
    @TableId(value = "blogs_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long blogsId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "标签id")
    private Long tagsId;

    /*@TableField(exist = false)  //查询时过滤非数据库字段
    @ApiModelProperty(value = "博客对象")
    private TBlog tBlog;

    @TableField(exist = false)  //查询时过滤非数据库字段
    @ApiModelProperty(value = "标签对象")
    private TTag tTag;*/

}
