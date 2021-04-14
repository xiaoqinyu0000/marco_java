package com.amosyo.configure.microprofile.servlet;

import com.amosyo.configure.microprofile.source.ServletContextConfigSource;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileServletInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        final ServletContextConfigSource servletContextConfigSource = new ServletContextConfigSource(servletContext);
        servletContextConfigSource.initProperties();
        final ClassLoader classLoader = servletContext.getClassLoader();
        final ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        final ConfigBuilder builder = configProviderResolver.getBuilder();
        builder.forClassLoader(classLoader);
        builder.addDefaultSources();
        builder.addDiscoveredConverters();
        builder.withSources(servletContextConfigSource);
        final Config config = builder.build();
        configProviderResolver.registerConfig(config, classLoader);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
