package org.xjt.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiong
 * @ClassName TMessage.java
 * @createTime 2021/12/9
 * @Description 留言
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TMessage {
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "留言id")
    @TableId(type = IdType.ID_WORKER)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "留言者id")
    private Long creatorId;

    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "留言父id")
    private Long parentMessageId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/ShangHai")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    //留言的回复列表
    @TableField(exist = false)
    private List<TMessage> replayMessages = new ArrayList<>();
}
