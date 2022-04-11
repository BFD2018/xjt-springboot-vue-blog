package org.xjt.blog.controller;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xjt.blog.entity.TUser;
import org.xjt.blog.entity.TUserFile;
import org.xjt.blog.mapper.TUserFileMapper;
import org.xjt.blog.mapper.TUserMapper;
import org.xjt.blog.service.TUserFileService;
import org.xjt.blog.utils.MyUtils;
import org.xjt.blog.utils.RespBean;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/file")
public class TUserFileController {
    @Autowired
    private TUserFileService tUserFileService;

    @Autowired
    private TUserMapper tUserMapper;

    @PostMapping("/aliyun/oss")
    public RespBean uploadFile(@RequestParam("file") MultipartFile file){
        return tUserFileService.uploadImageToOss(file);
    }
    // 上传文件(已登录的用户)
    //1、文件保存到本地服务器上
    //2、文件信息保存到数据库记录上
    @PostMapping("/upload")
    public RespBean uploadFile(@RequestParam("file") MultipartFile file,
                               @RequestParam("user_id") String user_id,
                               HttpServletRequest request) throws IOException {
        log.warn("xjt--->uploadFile trigger");
        System.out.println(request.getRequestURL().toString());
        System.out.println("user_id=====================>"+user_id);
        if(!StringUtils.hasText(user_id)){
            return RespBean.warn("未获取到用户id");
        }

        //存储路径
        String storagePath = "fileContainer/";
        // 获取文件的原始名称
        String originalFilename = file.getOriginalFilename();
        // 获取文件的后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // 获取文件的大小
        long size = file.getSize();

        // 获取文件的类型
        String type_str = file.getContentType();
        log.warn("type_str===>"+type_str);
        Integer type = 0;
        if(type_str.startsWith("image")){
            type = 1;
        }
        else if(type_str.startsWith("text")){
            type = 2;
        }
        else if(type_str.startsWith("application") && (type_str.contains("ms") || type_str.contains("pdf") || type_str.contains("xml")))
        {
            type = 2;
        }
        else if(type_str.startsWith("audio")){
            type = 3;
        }
        else if(type_str.startsWith("video")){
            type = 4;
        }
        else if(type_str.contains("zip") || type_str.contains("tar") || type_str.contains("rar") || type_str.contains("7z") || type_str.contains("octet-stream"))
        {
            type = 5;
        }
        else
        {
            type = 6;
        }
        // 根据日期动态的生成目录
        String uploadPath = ResourceUtils.getURL("classpath").getPath() + storagePath + DateUtil.today();
        File dateDirPath = new File(uploadPath);
        if (!dateDirPath.exists()) {
            dateDirPath.mkdirs();
        }
        log.warn("上传文件路径："+dateDirPath.toString());
        //文件新名称
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "-" + originalFilename;

        // 处理文件上传到本地
        file.transferTo(new File(dateDirPath, newFileName));

        // 将文件信息存入数据库中
        TUserFile userFile = new TUserFile();
        userFile.setOldFileName(originalFilename)
                .setNewFileName(newFileName)
                .setExt('.' + extension)
                .setPath("/classpath/" + storagePath + DateUtil.today() + "/" + newFileName)
                .setSize(""+size)
                .setFileType(type)
                .setDownCounts(0)
                .setUserId(Long.valueOf(user_id));

        Integer insert = tUserFileService.uploadFile(userFile);
        if(insert >0){
            return RespBean.ok("上传成功",userFile);
        }else{
            return RespBean.error("文件上传失败");
        }
    }

    @PostMapping("/avatar/upload")
    public RespBean uploadTUserAvatar(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
        log.warn("xjt--->uploadTUserAvatar trigger");
        String id = request.getParameter("id");     //用户id

        //存储路径
        String storagePath = "fileContainer/avatarImages/";
        // 获取文件的原始名称
        String originalFilename = file.getOriginalFilename();
        // 获取文件的后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // 获取文件的大小
        long size = file.getSize();

        // 根据日期动态的生成目录
        String uploadPath = ResourceUtils.getURL("classpath").getPath() + storagePath + DateUtil.today();
        File dateDirPath = new File(uploadPath);
        if (!dateDirPath.exists()) {
            dateDirPath.mkdirs();
        }
        log.warn("上传文件路径："+dateDirPath.toString());
        //文件新名称
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "-" + originalFilename;

        // 处理文件上传到本地
        file.transferTo(new File(dateDirPath, newFileName));

        // 将文件信息存入数据库中
        TUserFile userFile = new TUserFile();
        userFile.setOldFileName(originalFilename)
                .setNewFileName(newFileName)
                .setExt('.' + extension)
                .setPath("/classpath/" + storagePath + DateUtil.today() + "/" + newFileName)
                .setSize(""+size)
                .setFileType(1)
                .setDownCounts(0)
                .setUserId(Long.valueOf(id));

        Integer insert = tUserFileService.uploadFile(userFile);
        if(insert >0){
            return RespBean.ok("上传成功",userFile);
        }else{
            return RespBean.error("文件上传失败");
        }
    }

    //获取用户所有文件
    @GetMapping("/all")
    public RespBean findAll(HttpSession session){
        log.warn("xjt--->trigger findUserAllFile");
        //在登入的session中获取Id
        TUser user = (TUser) session.getAttribute("login_user");
        return tUserFileService.getAllFilesByUserId(user.getId());
    }

    // 文件下载
    @GetMapping("/download")
    public void download(@RequestParam("id") Long id, HttpServletResponse response){
        String openStyle = "attachment";
        log.warn("xjt--->trigger download");
        try{
            getFile(openStyle,id,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //后端-获取所有上传的文件数量
    @GetMapping("/back/allNum")
    public RespBean backGetAllNum(){
        return tUserFileService.backGetAllNum();
    }

    // 文件预览
    @GetMapping("/preview")
    public void preview(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        System.out.println("id===>"+id);

        String openStyle = "inline";
        getFile(openStyle,id,response);
    }
    public void getFile(String openStyle, Long id, HttpServletResponse response) throws IOException {
        TUserFile userFile =  tUserFileService.getByUserFileId(id);
        // 获取文件信息
        final String realPath = ResourceUtils.getURL("").getPath() + userFile.getPath();
        // 获取文件输入流
        FileInputStream is = new FileInputStream(realPath);
        // 附件下载
        response.setHeader("content-disposition", openStyle+";filename=" + URLEncoder.encode(userFile.getOldFileName(), "UTF-8"));
        // 获取响应response输出流
        ServletOutputStream os = response.getOutputStream();
        // 文件拷贝
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
        // 更新下载次数
        if(openStyle.equals("attachment")){
            userFile.setDownCounts(userFile.getDownCounts() + 1);
            tUserFileService.updateUserFile(userFile);
        }
    }

    @Autowired
    private TUserFileMapper tUserFileMapper;

    //kkFileView文件预览
    @GetMapping("/kkfile/preview")
    public RespBean kkFilePreview(@RequestParam("id") Long id) throws IOException {
        System.out.println("id===>"+id);
        TUserFile userFile = tUserFileMapper.selectById(id);

        MyUtils myUtils = new MyUtils();

        String realPath = myUtils.getUrl() + userFile.getPath();
        System.out.println(realPath);

        return RespBean.ok("ok",realPath);
    }

    // 文件删除
    @GetMapping("delete")
    public RespBean deleteByFileId(@RequestParam("file_id") Long file_id) {
        System.out.println(file_id);
        try{
            TUserFile fileInfo = tUserFileService.getByUserFileId(file_id);
            String realPath = ResourceUtils.getURL("classpath").getPath() + fileInfo.getPath();
            File file = new File(realPath);
            //1、删除数据库记录
            tUserFileService.deleteUserFile(file_id);
            //2、删除本地文件
            if(file.exists()){
                file.delete();
            }
            return RespBean.ok("删除成功！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return RespBean.ok("删除失败！",e);
        }
    }

    @GetMapping("/test")
    public void test01(HttpServletRequest request){
        System.out.println(request.getServletPath());       //  /file/test

        System.out.println(request.getRequestURI());        //  /file/test

        System.out.println(request.getProtocol()+"  "+request.getRemoteHost());        //HTTP/1.1://0:0:0:0:0:0:0:1

        System.out.println(request.getRequestURL());        // http://localhost:8000/file/test
        System.out.println(request.getContextPath());       //  /
        System.out.println(request.getScheme());        //http
        System.out.println(request.getServerPort());        //8000
        System.out.println(request.getServerName());        //localhost
        System.out.println(request.getServletContext().getAttribute(""));        //获取存到Model中的值

    }

    /*按文件类型分页查询*/
    @GetMapping("/queryByConditions")
    public RespBean getFileByPage(
            @RequestParam(value = "current",defaultValue = "1") Integer current,
            @RequestParam(value = "pageSize",defaultValue = "6") Integer pageSize,
            @RequestParam(value = "type",required = false) String type,
            @RequestParam(value = "user_id",required = true) String user_id
        ){
        log.warn("current===>"+current);
        log.warn("pageSize===>"+pageSize);
        log.warn("file_type===>"+type);
        return tUserFileService.queryFileByCondition(current,pageSize,type,user_id);
    }

    /*折线图：根据用户和类型分组查询文件数量*/
    @GetMapping("/getCountsByUserAndType")
    public RespBean getCountsGroupByUserAndType(){
        return tUserFileService.getCountsGroupByUserAndType();
    }
}
