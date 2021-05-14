package com.amosyo.application.user.projects.user.exception;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class RegisterUserException extends RuntimeException{

    private final String msg;

    public RegisterUserException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
