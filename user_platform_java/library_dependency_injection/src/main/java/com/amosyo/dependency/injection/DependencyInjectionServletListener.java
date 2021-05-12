package com.amosyo.dependency.injection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class DependencyInjectionServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ComponentContext componentContext = new ComponentContext();
        componentContext.init(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
