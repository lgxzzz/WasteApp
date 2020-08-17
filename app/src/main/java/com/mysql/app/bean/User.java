package com.mysql.app.bean;

import java.io.Serializable;

/***
 * 封装的用户对象
 *
 * */
public class User  implements Serializable {
    private String UserId;
    private String UserName;
    private String Password;
    private String RepeatPassword;
    private String Email;
    private String Role;


    public String getRepeatPassword() {
        return RepeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        RepeatPassword = repeatPassword;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
