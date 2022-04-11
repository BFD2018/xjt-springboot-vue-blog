package org.xjt.blog.service;

import org.springframework.web.multipart.MultipartFile;
import org.xjt.blog.entity.TUserFile;
import org.xjt.blog.utils.RespBean;

/**
 * @author xiong
 * @ClassName TUserFileUploadService.java
 * @createTime 2021/11/8
 * @Description TODO
 */
public interface TUserFileService {
    Integer uploadFile(TUserFile userFile);

    RespBean getAllFilesByUserId(Long id);

    TUserFile getByUserFileId(Long id);

    void updateUserFile(TUserFile userFile);

    void deleteUserFile(Long id);

    RespBean getCountsGroupByUserAndType();


    RespBean queryFileByCondition(Integer current, Integer pageSize, String type, String user_id);

    RespBean backGetAllNum();

    RespBean uploadImageToOss(MultipartFile file);
}
