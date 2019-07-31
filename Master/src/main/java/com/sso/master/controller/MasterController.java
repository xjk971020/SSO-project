package com.sso.master.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

/**
 * @author:xjk 2019/7/31 18:35
 * com.sso.master.controller
 */
@RestController
@RequestMapping("/master")
public class MasterController {

    @GetMapping("/login")
    public String login() {
        return "进行登录的页面";
    }

    @GetMapping("/doLogin")
    public String doLogin(@RequestParam(value = "username") String userName,
                          @RequestParam(value = "password") String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            return "密码错误";
        }
        return "登陆成功";
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "退出成功";
    }

    @GetMapping("/authc")
    public String authc() {
        return "master项目中必须登录才能访问的页面";
    }
}
