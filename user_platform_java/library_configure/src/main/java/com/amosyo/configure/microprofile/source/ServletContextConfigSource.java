package com.amosyo.configure.microprofile.source;

import org.checkerframework.checker.nullness.qual.NonNull;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ServletContextConfigSource extends AbstractConfigSource {

    private final ServletContext servletContext;

    public ServletContextConfigSource(@NonNull final ServletContext servletContext) {
        super();
        this.servletContext = requireNonNull(servletContext, "servletContext");
    }

    @Override
    protected @NonNull Map<String, String> loadProperties() {
        final Enumeration<String> initParameterNames = this.servletContext.getInitParameterNames();
        final Map<String, String> rPropertiesMap = new HashMap<>();
        String paramName;
        while (initParameterNames.hasMoreElements()) {
            paramName = initParameterNames.nextElement();
            rPropertiesMap.put(paramName, this.servletContext.getInitParameter(paramName));
        }
        return rPropertiesMap;
    }

    @Override
    public int getOrdinal() {
        return 400;
    }

    @Override
    public String getName() {
        return "ServletContext Init Parameter";
    }
}
