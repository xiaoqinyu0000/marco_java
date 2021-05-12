package com.amosyo.configure.microprofile.servlet;

import org.eclipse.microprofile.config.Config;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileConfigInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        servletContext.addListener(MicroprofileServletInitializerListener.class);
        servletContext.addListener(MicroprofileConfigServletRequestListener.class);
    }
}
