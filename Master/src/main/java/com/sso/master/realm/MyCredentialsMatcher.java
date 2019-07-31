package com.sso.master.realm;

import com.sso.master.entity.User;
import com.sso.master.service.UserService;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author:xjk 2019/7/31 18:21
 * com.sso.master.realm
 */
@Data
public class MyCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    private UserService userService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
//        return super.doCredentialsMatch(token, info);
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        User user = userService.findUserByUserName(userToken.getUsername());
        char[] passwordChars =userToken.getPassword();
        String password = new String(passwordChars);
        return userService.verifyPassword(user, password);
    }
}
