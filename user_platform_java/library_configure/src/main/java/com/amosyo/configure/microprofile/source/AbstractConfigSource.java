package com.amosyo.configure.microprofile.source;

import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public abstract class AbstractConfigSource implements ConfigSource {

    @NonNull
    private final Map<String, String> properties = new HashMap<>(0);

    public void initProperties() {
        this.properties.clear();
        this.properties.putAll(loadProperties());
    }

    @NonNull
    protected abstract Map<String, String> loadProperties();

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    @Override
    public Set<String> getPropertyNames() {
        return this.properties.keySet();
    }

    @Override
    @NonNull
    public String getValue(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        return properties.getOrDefault(s, "");
    }

}
