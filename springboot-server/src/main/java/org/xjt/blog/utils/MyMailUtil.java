package org.xjt.blog.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;

public class MyMailUtil {
    /**
     * 发送验证码邮件 返回验证码
     * @param to
     * @return
     */
    public static String sendMail(String to){
        String code = RandomUtil.randomString(4);
        String content = "您正在登陆小熊的空间（3分钟内有效），验证码为：" + "【" + code + "】";

        MailUtil.sendText(to,"【小熊空间】",content);

        return code;
    }
}
