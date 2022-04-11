package org.xjt.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value="TLinks对象", description="")
public class TLinks implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)        //将Long类型序列化时转化为字符串，保留相关精度
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "博客地址")
    private String blogAddress;

    @ApiModelProperty(value = "博客名称")
    private String blogName;

    @ApiModelProperty(value = "首图")
    private String pictureAddress;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime updateTime;

}

