package com.bright.spring.learn.sale;

import java.util.Date;

/**
 * 商品用户
 */
public class SaleUser {
    // 用户id,手机号码
    private long id;
    // 昵称
    private String nickname;
    // MD5(pass明文+固定salt)+salt
    private String password;
    // 盐值
    private String salt;
    // 头像，云存储ID
    private String head;
    // 注册时间
    private Date registerDate;
    // 上次登录时间
    private Date lastLoginTime;
    // 登录次数
    private int loginCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }
}
