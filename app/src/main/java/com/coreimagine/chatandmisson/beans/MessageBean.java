package com.coreimagine.chatandmisson.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 离线缓存
 */
@Table(name = "tasks")
public class MessageBean {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    @Column(name = "time")
    private String time;    //日期
    @Column(name = "user_name")
    private String user_name;   //登陆人姓名
    @Column(name = "group")
    private String group;   //登陆人姓名
    @Column(name = "message")
    private String message;   //登陆人姓名
    @Column(name = "type")
    private int type;
    @Column(name = "login_name")
    private String loginName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
