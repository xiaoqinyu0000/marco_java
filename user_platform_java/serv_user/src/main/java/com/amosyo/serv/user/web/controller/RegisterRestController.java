package com.amosyo.serv.user.web.controller;

import com.amosyo.library.mvc.controller.RestController;
import com.amosyo.library.mvc.jmx.JMXManager;
import com.amosyo.serv.user.projects.user.bo.UserBO;
import com.amosyo.serv.user.projects.user.domain.User;
import com.amosyo.serv.user.projects.user.exception.RegisterUserException;
import com.amosyo.serv.user.projects.user.service.UserService;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
@Path("/register.do")
public class RegisterRestController implements RestController {

    @Resource(name = "service/UserService")
    private UserService userService;

    @POST
    @Path("")
    public void doPostRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String id = request.getParameter("id");
        final String name = request.getParameter("name");
        final String password = request.getParameter("password");
        final String phoneNumber = request.getParameter("phoneNumber");
        final UserBO user = new UserBO();
        user.setName(name);
        user.setId(id);
        user.setPassword(password);
        user.setEmail("");
        user.setPhoneNumber(phoneNumber);
        JMXManager.getInstance().tryRegisterPojo(user);

        try {
            userService.register(user);
        } catch (RegisterUserException registerUserException) {
            doResultText(response, makeFailedHtml(registerUserException.getMsg()));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            doResultText(response, makeFailedHtml("请检查网络情况"));
            return;
        }

        doResultText(response, makeSuccessHtml());
    }

    private void doResultText(HttpServletResponse response, String text) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        response.getWriter().write(text);
        response.flushBuffer();
    }

    @NonNull
    private String makeFailedHtml(@NonNull final String reason) {
        return new StringBuilder()
                .append("<html>\n")
                .append("<head>\n")
                .append("    <title>首页</title>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("<h3>注册失败，理由：</h3>").append(reason).append("\n")
                .append("</body>\n")
                .toString();
    }

    @NonNull
    private String makeSuccessHtml() {
        return new StringBuilder()
                .append("<html>\n")
                .append("<head>\n")
                .append("    <title>首页</title>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("<h3>注册成功\n</h3>")
                .append("</body>\n")
                .toString();
    }
}
