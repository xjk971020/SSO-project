package com.sso.master.util;

import com.sso.master.entity.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author:xjk 2019/7/31 18:16
 * com.sso.master.util
 */
public class PasswordUtil {

    /**
     * 实例化RandomNumberGenerator对象，用于生成一个随机数
     */
    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    /**
     * 散列算法名称
     */
    private static  String algorithName = "MD5";

    /**
     * 散列次数
     */
    private static int hashIterations = 1024;


    /**
     * 给用户加密
     * @param user
     */
    public static void encryptPassword(User user) {
        if (user.getPassword() != null) {
            user.setSalt(randomNumberGenerator.nextBytes().toHex());
            System.out.println("salt： " + user.getSalt());
        }
        String password = new SimpleHash(algorithName,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),hashIterations).toHex();
        user.setPassword(password);
        System.out.println("password: " + user.getPassword());
    }

    private static String encodePassword(String pwd, String salt) {
        String newPassword = new SimpleHash(
                algorithName,
                pwd,
                ByteSource.Util.bytes(salt),
                hashIterations).toHex();
        return newPassword;
    }

    /**
     * 进行前端接收的密码和数据库密码的必比对
     * @param targetPassword
     * @param pwd
     * @param salt
     * @return
     */
    public static boolean verifyPassword(String targetPassword, String pwd, String salt){
        String newPassword = encodePassword(targetPassword, salt);
        if(newPassword.equals(pwd)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.setPassword("123456");
        PasswordUtil.encryptPassword(user);
    }
}
