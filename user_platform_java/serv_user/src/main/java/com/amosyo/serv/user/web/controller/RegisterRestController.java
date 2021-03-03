package com.amosyo.serv.user.web.controller;

import com.amosyo.library.mvc.controller.RestController;
import com.amosyo.serv.user.projects.user.domain.User;
import com.amosyo.serv.user.projects.user.service.UserService;
import com.amosyo.serv.user.projects.user.service.impl.UserServiceImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    private final UserService userService = new UserServiceImpl();

    @POST
    @Path("")
    public void doPostRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String name = request.getParameter("name");
        final String password = request.getParameter("password");
        if (isBlank(name)) {
            doResultText(response, makeFailedHtml("用户名为null"));
            return;
        }
        if (isBlank(password)) {
            doResultText(response, makeFailedHtml("密码为null"));
            return;
        }
        final User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail("");
        user.setPhoneNumber("");

        try {
            userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            doResultText(response, makeFailedHtml("请检查网络情况"));
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
