package org.xjt.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xjt.blog.config.AliyunOssConfig;
import org.xjt.blog.entity.TUserFile;
import org.xjt.blog.mapper.TUserFileMapper;
import org.xjt.blog.service.TUserFileService;
import org.xjt.blog.utils.RespBean;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TUserFileServiceImpl implements TUserFileService {
    @Autowired
    private TUserFileMapper tUserFileMapper;

    @Autowired
    private OSS ossClient;          // 注入阿里云oss文件服务器客户端

    @Autowired
    private AliyunOssConfig aliyunOssConfig;// 注入阿里云OSS基本配置类

    @Override
    public Integer uploadFile(TUserFile userFile) {
        userFile.setUploadTime(DateUtil.now());
        int insert = tUserFileMapper.insert(userFile);
        return insert;
    }

    @Override
    public RespBean getAllFilesByUserId(Long id) {
        List<TUserFile> fileList = tUserFileMapper.selectList(new QueryWrapper<TUserFile>().eq("user_id",id));
        return RespBean.ok("成功获取用户所有文件",fileList);
    }

    @Override
    public TUserFile getByUserFileId(Long id) {
        TUserFile userFile = tUserFileMapper.selectById(id);

        return userFile;
    }

    @Override
    public void updateUserFile(TUserFile userFile) {
        tUserFileMapper.updateById(userFile);
    }

    @Override
    public void deleteUserFile(Long id) {
        int i = tUserFileMapper.deleteById(id);
    }

    @Override
    public RespBean getCountsGroupByUserAndType() {
        List<Map<String, Object>> mapList = tUserFileMapper.getCountsGroupByUserAndFileType();
        if(ObjectUtils.isEmpty(mapList)){
            return RespBean.warn("error");
        }else{
            return RespBean.ok("ok",mapList);
        }
    }

    @Override
    public RespBean queryFileByCondition(Integer current, Integer pageSize, String type, String user_id) {
        Page<TUserFile> page = new Page<>(current,pageSize);
        QueryWrapper<TUserFile> wrapper = new QueryWrapper<>();

        if(!ObjectUtils.isEmpty(user_id) && StringUtils.hasText(user_id)){
            wrapper.eq("user_id",user_id);
        }
        if(!ObjectUtils.isEmpty(type) && StringUtils.hasText(type) && !"0".equalsIgnoreCase(type)){
            System.out.println("type===================>"+type);
            wrapper.eq("file_type",type);
        }

        tUserFileMapper.selectPage(page,wrapper);
        List<TUserFile> records = page.getRecords();
        if(ObjectUtils.isEmpty(records)){
            return RespBean.warn("empty");
        }else{
            return RespBean.ok("ok",page);
        }
    }

    @Override
    public RespBean backGetAllNum() {
        Integer count = tUserFileMapper.selectCount(null);
        if(count<0){
            return RespBean.warn("empty");
        }else{
            return RespBean.ok("ok",count);
        }
    }

    @Override
    public RespBean uploadImageToOss(MultipartFile file) {
        // 获取oss的Bucket名称
        String bucketName = aliyunOssConfig.getBucketName();
        // 获取oss的地域节点
        String endpoint = aliyunOssConfig.getEndPoint();
        // 获取oss目标文件夹
        String filehost = aliyunOssConfig.getFileHost();

        String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

        // 返回图片上传后返回的url
        String returnImgeUrl = "";

        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {// 如果图片格式不合法
            return RespBean.error("图片格式不合法");
        }
        // 获取文件原名称
        String originalFilename = file.getOriginalFilename();
        // 获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 新文件名称
        String newFileName = UUID.randomUUID().toString().replace("-","") + fileType;

        // 构建日期路径, 例如：OSS目标文件夹  /2022/10/文件名
        String filePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        // 文件上传的路径地址
        String uploadImgeUrl = filehost + "/" + filePath + "/" + newFileName;

        // 获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(!ossClient.doesBucketExist(bucketName)){
                ossClient.createBucket(bucketName);
                CreateBucketRequest bucketRequest = new CreateBucketRequest(bucketName);
                bucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(bucketRequest);
            }
            //文件上传至阿里云OSS
            ossClient.putObject(bucketName, uploadImgeUrl, inputStream, null);

            // 获取文件上传后的图片返回地址
            returnImgeUrl = "http://" + bucketName + "." + endpoint + "/" + uploadImgeUrl;

            return RespBean.ok("ok",returnImgeUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("error",e.getMessage());
        }
    }
}
