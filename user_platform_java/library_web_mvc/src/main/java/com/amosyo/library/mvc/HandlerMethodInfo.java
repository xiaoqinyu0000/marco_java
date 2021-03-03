package com.amosyo.library.mvc;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class HandlerMethodInfo {

    private final String requestPath;
    private final Method requestMethod;
    private final Set<String> supportHttpMethods;

    public HandlerMethodInfo(String requestPath, Method requestMethod, Set<String> supportHttpMethods) {
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
        this.supportHttpMethods = supportHttpMethods;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Method getRequestMethod() {
        return requestMethod;
    }

    public Set<String> getSupportHttpMethods() {
        return supportHttpMethods;
    }
}
