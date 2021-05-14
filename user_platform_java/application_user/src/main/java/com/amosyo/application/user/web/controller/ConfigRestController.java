package com.amosyo.application.user.web.controller;

import com.amosyo.configure.ConfigFace;
import com.amosyo.library.mvc.controller.RestController;
import org.eclipse.microprofile.config.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
@Path("/config")
public class ConfigRestController implements RestController {

    private static final String KEY_APPLICATION_NAME = "application.name";
    private static final String KEY_AUTHOR = "author";

    @GET
    @Path("")
    public void doGetConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final Config config = ConfigFace.getConfig();
        final String applicationName = config.getValue(KEY_APPLICATION_NAME, String.class);
        final String anchor = config.getValue(KEY_AUTHOR, String.class);
        final String text = String.format(
                "<html>\n" +
                        "<head>\n" +
                        "    <title>配置中心</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h3>application is %s\n</h3>" +
                        "<h3>anchor is %s\n</h3>" +
                        "</body>\n",
                applicationName, anchor);

        doResultText(response, text);
    }

    private void doResultText(HttpServletResponse response, String text) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        response.getWriter().write(text);
        response.flushBuffer();
    }
}
