package org.xjt.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.xjt.blog.config.shiro.UserEmailToken;
import org.xjt.blog.entity.TUser;
import org.xjt.blog.entity.TUserFile;
import org.xjt.blog.entity.TUserRole;
import org.xjt.blog.mapper.TUserFileMapper;
import org.xjt.blog.mapper.TUserMapper;
import org.xjt.blog.service.TRoleService;
import org.xjt.blog.service.TUserRoleService;
import org.xjt.blog.service.TUserService;
import org.xjt.blog.utils.MyMailUtil;
import org.xjt.blog.utils.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/user")
public class TUserController {
    @Autowired
    private TUserService tUserService;

    @Autowired
    private TUserMapper tUserMapper;

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
        System.out.println(params);
        //1、用户名密码登录
        String username = params.get("username");
        String password = params.get("password");
        String verify_code = params.get("verify_code");

        //2、邮箱登录
        String email = params.get("email");
        String email_code = params.get("code");

        if(StringUtils.hasText(username) && StringUtils.hasText(password) && StringUtils.hasText(verify_code)){
            try {
                //比较验证码
                String codes = (String) session.getAttribute("verify_code");
                log.warn("session中保存的验证码："+codes);
                log.warn("用户输入的验证码："+verify_code);

                if (codes.equalsIgnoreCase(verify_code)){
                    //获取主体对象
                    Subject subject = SecurityUtils.getSubject();

                    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
                    subject.login(token);

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
        else if(StringUtils.hasText(email) && StringUtils.hasText(email_code)){
            //2、邮箱登录
            String code = (String) session.getAttribute("email_code");
            log.warn("session中保存的邮箱验证码："+code);
            log.warn("用户输入的邮箱验证码："+email_code);

            if (code.equals(email_code)){
                //获取主体对象
                Subject subject2 = SecurityUtils.getSubject();

                UserEmailToken userEmailToken = new UserEmailToken(email);
                subject2.login(userEmailToken);

                TUser login_user = (TUser)SecurityUtils.getSubject().getSession().getAttribute("login_user");
                log.warn("将已登录成功的用户存入到session中 tUser="+login_user.toString());

                // 销毁验证码
                session.removeAttribute("email_code");

                return RespBean.ok("登录成功！",login_user);
            }else{
                log.error("验证码错误!");
                return RespBean.warn("验证码错误！");
            }
        }
        else{
            return RespBean.error("请检查是否正确输入");
        }
    }

    @GetMapping("/sendMailCode")
    @ResponseBody
    public RespBean sendMailCode(@RequestParam("email") String email,HttpSession session){
        String mailCode = MyMailUtil.sendMail(email);
        session.setAttribute("email_code",mailCode);
        session.setMaxInactiveInterval(60*3);     //设置3分钟有效

        return RespBean.ok("邮件发送成功，请速去邮箱查看！");
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
