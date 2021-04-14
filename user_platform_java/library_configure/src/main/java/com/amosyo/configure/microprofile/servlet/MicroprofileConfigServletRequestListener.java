package com.amosyo.configure.microprofile.servlet;

import com.amosyo.configure.ConfigFace;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileConfigServletRequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        final ServletRequest servletRequest = servletRequestEvent.getServletRequest();
        final ServletContext servletContext = servletRequest.getServletContext();
        final ClassLoader classLoader = servletContext.getClassLoader();
        final Config config = ConfigProviderResolver.instance().getConfig(classLoader);
        ConfigFace.register(config);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        ConfigFace.unRegister();
    }
}
