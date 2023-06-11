package org.xjt.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value="评论TComment", description="")
public class TComment implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)        //序列化时,将Long保存为String 保留精度
    @ApiModelProperty(value = "评论id")
    @TableId(type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "用户id（评论者）")
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "博客id")
    private Long blogId;

    @JsonSerialize(using = ToStringSerializer.class) //系统序列化时，保留相关精度
    @ApiModelProperty(value = "父评论id")
    private Long parentCommentId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/ShangHai")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime ;

    //评论的回复列表
    @TableField(exist = false)
    private List<TComment> replayComments = new ArrayList<>();
}

