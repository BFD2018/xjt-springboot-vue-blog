package org.xjt.blog.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xjt.blog.entity.TUser;
import org.xjt.blog.entity.TUserFile;
import org.xjt.blog.entity.TUserRole;
import org.xjt.blog.mapper.TUserFileMapper;
import org.xjt.blog.service.TRoleService;
import org.xjt.blog.service.TUserRoleService;
import org.xjt.blog.service.TUserService;
import org.xjt.blog.utils.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/user")
public class TUserController {
    @Autowired
    private TUserService tUserService;

    //获取所有用户
    @ResponseBody
    @GetMapping("/all")
    public RespBean getAll(){
        return tUserService.getAllUser();
    }

    //按用户名模糊搜索
    @ResponseBody
    @GetMapping("/getByName")
    public RespBean getUserListByName(@RequestParam("name") String name){
        return tUserService.getUserListByName(name);
    }

    //博主登录
    @PostMapping("/login")
    @ResponseBody
    public RespBean login(@RequestBody HashMap<String,String> params, HttpSession session) {
        String username = params.get("username");
        String password = params.get("password");
        String verify_code = params.get("verify_code");
        System.out.println(params);
        //比较验证码
        String codes = (String) session.getAttribute("verify_code");
        log.warn("session中保存的验证码："+codes);
        log.warn("用户输入的验证码："+verify_code);

        try {
            if (codes.equalsIgnoreCase(verify_code)){
                //获取主体对象
                Subject subject = SecurityUtils.getSubject();

                subject.login(new UsernamePasswordToken(username, password));

                //用户登录成功，将tUser返回给前端
                TUser tUser = tUserService.findByUserName((String) subject.getPrincipal());

                //将登陆用户存到session中
                session.setAttribute("login_user",tUser);

                return RespBean.ok("登录成功！",tUser);
            }else{
                log.error("验证码错误!");
                return RespBean.warn("验证码错误！");
            }
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return RespBean.error("用户名错误！");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误!");
            return RespBean.error("密码错误！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return RespBean.error(e.getMessage());
        }
    }

    //游客登录
    @PostMapping("/guest/login")
    @ResponseBody
    public RespBean guestLogin(@RequestBody HashMap<String,String> params, HttpSession session) {
        String username = params.get("username");
        String password = params.get("password");

        try {
            //获取主体对象
            Subject subject = SecurityUtils.getSubject();

            subject.login(new UsernamePasswordToken(username, password));

            //登录成功，将tUser返回给前端
            TUser tUser = tUserService.findByUserName((String) subject.getPrincipal());

            //将登陆用户存到session中
            session.setAttribute("login_guest",tUser);
            return RespBean.ok("登录成功！",tUser);

        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return RespBean.error("用户名错误！");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误!");
            return RespBean.error("密码错误！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return RespBean.error(e.getMessage());
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public RespBean logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();       //退出用户
            return RespBean.ok("退出登录！");
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("退出失败",e.getMessage());
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public RespBean register(@RequestBody HashMap<String,String> params) {
        String username = params.get("username").trim();
        String password = params.get("password").trim();

        try {
            return tUserService.register(new TUser().setUsername(username).setPassword(password));
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("创建失败",e.getMessage());
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public RespBean deleteUserById(@RequestParam("id") String id) {
        return tUserService.deleteUserById(id);
    }

    /** 已存在username password之后更新用户其他信息
     * @Author xiong
     * @Description //TODO
     * @Date 2021/11/23
     * @return org.xjt.blog.utils.RespBean
     **/
    @PostMapping("/update/more")
    @ResponseBody
    public RespBean updateMore(@RequestBody HashMap<String,String> params) {
        String uid = params.get("id").trim();
        String nickname = params.get("nickname").trim();
        String avatar = params.get("avatar").trim();
        String email = params.get("email").trim();
        String description = params.get("description").trim();
        String roleId = params.get("role").trim();
        System.out.println(params);

        TUser tUser = new TUser().setId(Long.valueOf(uid))
                .setNickname(nickname).setEmail(email).setAvatar(avatar).setDescription(description);

        TUserRole tUserRole = new TUserRole().setUserid(Long.valueOf(uid)).setRoleid(Long.valueOf(roleId));

        try {
            tUserRoleService.add(tUserRole);

            return tUserService.updateByUid(tUser);
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("创建失败",e.getMessage());
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public RespBean updateTUser(@RequestBody TUser tUser) {
        System.out.println(tUser);
        return tUserService.updateByUid(tUser);
    }



    @RequestMapping("/getCaptcha")
    public void getCaptchaImg(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        SpecCaptcha specCaptcha = new SpecCaptcha();
        specCaptcha.setLen(4);
        specCaptcha.setHeight(48);
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String text = specCaptcha.text();
        log.warn("getCaptchaImg生成的验证码："+text);
        session.setAttribute("verify_code",text);
        CaptchaUtil.out(specCaptcha,request,response);
    }

    /*t_role角色表*/
    @Autowired
    private TRoleService tRoleService;

    @GetMapping("/role/all")
    @ResponseBody
    public RespBean getAllRoles(){
        return tRoleService.getAllRoles();
    }

    //角色对应的所有用户数量
    @GetMapping("/counts/byRole")
    @ResponseBody
    public RespBean getUserCountsByRole(){
        return tRoleService.getUserCountsByRole();
    }

    /*t_user_role用户角色表*/
    @Autowired
    private TUserRoleService tUserRoleService;

    @GetMapping("/tUserRole/add")
    @ResponseBody
    public RespBean handleSave(@RequestBody TUserRole tUserRole){
        return tUserRoleService.add(tUserRole);
    }
}
