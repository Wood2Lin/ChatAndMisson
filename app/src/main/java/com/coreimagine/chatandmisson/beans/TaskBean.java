package com.coreimagine.chatandmisson.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 离线缓存
 */
@Table(name = "tasks")
public class TaskBean {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    @Column(name = "Num")
    private String Num;    //日期
    @Column(name = "flag")
    private String flag;   //登陆人姓名
    @Column(name = "groupName")
    private String groupName;   //登陆人姓名
    @Column(name = "群组一")
    private String 群组一;   //登陆人姓名
    @Column(name = "taskID")
    private String taskID;
    @Column(name = "time")
    private String time;
    @Column(name = "type")
    private String type;
    @Column(name = "userID")
    private String userID;
    @Column(name = "userName")
    private String userName;
    @Column(name = "value1")
    private String value1;
    @Column(name = "value2")
    private String value2;
    @Column(name = "msg")
    private String msg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String get群组一() {
        return 群组一;
    }

    public void set群组一(String 群组一) {
        this.群组一 = 群组一;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
