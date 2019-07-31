package com.sso.follower.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:xjk 2019/7/31 20:09
 * com.sso.follower.controller
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @RequestMapping("/authc")
    public String authc() {
        return "follow项目中必须经过master项目的登录才能看到的页面";
    }

    @GetMapping("/anon")
    public String login() {
        return "follow项目中不用经过登录就能看到的页面";
    }
}
