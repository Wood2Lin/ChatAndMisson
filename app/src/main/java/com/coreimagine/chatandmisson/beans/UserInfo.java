package com.coreimagine.chatandmisson.beans;

public class UserInfo {
    private Integer id;
    private String name;
    private int age;
    private int sex;
    private String intro;
    private String avatar;
    private String phone;
    private String loginname;
    private int usertype;
    private String createtime;
    private String password;
    private String groupname;

    public UserInfo() {}

    public UserInfo(String name, int age, int sex, String intro, String avatar, String phone) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.intro = intro;
        this.avatar = avatar;
        this.phone = phone;
    }

    public UserInfo(String password, String loginname) {
        this.password = password;
        this.loginname = loginname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
//    public String getRegion() {
//        return region;
//    }
//
//    public void setRegion(String region) {
//        this.region = region;
//    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }


    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreattime(String createtime) {
        this.createtime = createtime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
