package com.sso.master.service;

import com.sso.master.entity.User;
import com.sso.master.util.PasswordUtil;
import org.springframework.stereotype.Service;

/**
 * @author:xjk 2019/7/31 18:12
 * com.sso.master.service
 */
@Service
public class UserService {

    public boolean verifyPassword(User user, String password) {
        return PasswordUtil.verifyPassword(password,user.getPassword(),user.getSalt());
    }

    /**
     * 模拟从数据库中取出数据
     * @param userName
     * @return
     */
    public User findUserByUserName(String userName) {
        User user = new User();
        user.setUserName("cathetine");
        user.setPassword("23d2b8bf91110d5eb230183879e71e57");
        user.setSalt("e078d67a6a5fbb6b17617eed52f4cddc");
        return user;
    }
}
