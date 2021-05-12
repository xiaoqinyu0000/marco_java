package com.amosyo.serv.user.config;

import com.amosyo.dependency.injection.jmx.annotation.JMXManagedAttribute;
import com.amosyo.dependency.injection.jmx.annotation.JMXManagedOperation;
import com.amosyo.dependency.injection.jmx.annotation.JMXManagedResource;

import java.util.StringJoiner;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
@JMXManagedResource(objectName = "srv_user_platform:name=config")
public class UserConfig {

    @JMXManagedAttribute(description = "用户默认昵称")
    private String userDefaultNick;
    @JMXManagedAttribute(description = "用户默认头像")
    private String userDefaultAvatar;
    @JMXManagedAttribute(description = "用户默认性别")
    private String userDefaultGender;
    @JMXManagedAttribute(description = "用户Id增长步数")
    private int userIdStep;

    public UserConfig() {
        this.userDefaultNick = "nick";
        this.userDefaultAvatar = "avatar";
        this.userDefaultGender = "male";
        this.userIdStep = 20;
    }

    public String getUserDefaultNick() {
        return userDefaultNick;
    }

    public void setUserDefaultNick(String userDefaultNick) {
        this.userDefaultNick = userDefaultNick;
    }

    public String getUserDefaultAvatar() {
        return userDefaultAvatar;
    }

    public void setUserDefaultAvatar(String userDefaultAvatar) {
        this.userDefaultAvatar = userDefaultAvatar;
    }

    public String getUserDefaultGender() {
        return userDefaultGender;
    }

    public void setUserDefaultGender(String userDefaultGender) {
        this.userDefaultGender = userDefaultGender;
    }

    public int getUserIdStep() {
        return userIdStep;
    }

    public void setUserIdStep(int userIdStep) {
        this.userIdStep = userIdStep;
    }

    @Override
    @JMXManagedOperation(description = "用户配置详细数据")
    public String toString() {
        return new StringJoiner(", ", UserConfig.class.getSimpleName() + "[", "]")
                .add("userDefaultNick='" + userDefaultNick + "'")
                .add("userDefaultAvatar='" + userDefaultAvatar + "'")
                .add("userDefaultGender='" + userDefaultGender + "'")
                .add("userIdStep=" + userIdStep)
                .toString();


    }
}
