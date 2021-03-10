package com.amosyo.serv.user.projects.user.bo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class UserBO {

    @Min(value = 1, message = "用户Id必须为数字且大于0")
    private String id;
    @NotBlank(message = "用户名不能为null")
    private String name;
    @Length(min = 6, max = 32, message = "密码：6-32 位")
    @NotBlank(message = "密码不能为null")
    private String password;
    private String email;
    @NotBlank(message = "手机号码不能为null")
    @Pattern(regexp = "(^$|[0-9]{11})", message = "手机号码必须为11位数字")
    private String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
