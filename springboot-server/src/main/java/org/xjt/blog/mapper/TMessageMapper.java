package org.xjt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.xjt.blog.entity.TMessage;

import java.util.List;
import java.util.Map;

@Repository
public interface TMessageMapper extends BaseMapper<TMessage> {
    List<Map<String, Object>> selectLevel1Messages(String uid, String parent_message_id);

    List<Map<String, Object>> selectLevel1Replys(String uid, String parent_message_id);

    List<Map<String, Object>> selectLevel2Replys(String uid, String parent_message_id);
}
