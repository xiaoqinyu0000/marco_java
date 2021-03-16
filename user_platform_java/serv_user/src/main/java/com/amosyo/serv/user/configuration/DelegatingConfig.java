package com.amosyo.serv.user.configuration;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.List;
import java.util.Optional;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class DelegatingConfig implements Config {

    private final Config config;

    public DelegatingConfig() {
        this.config = ConfigProvider.getConfig();
    }

    @Override
    public <T> T getValue(String s, Class<T> aClass) {
        return config.getValue(s, aClass);
    }

    @Override
    public ConfigValue getConfigValue(String s) {
        return config.getConfigValue(s);
    }

    @Override
    public <T> List<T> getValues(String propertyName, Class<T> propertyType) {
        return config.getValues(propertyName, propertyType);
    }

    @Override
    public <T> Optional<T> getOptionalValue(String s, Class<T> aClass) {
        return config.getOptionalValue(s, aClass);
    }

    @Override
    public <T> Optional<List<T>> getOptionalValues(String propertyName, Class<T> propertyType) {
        return config.getOptionalValues(propertyName, propertyType);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return config.getPropertyNames();
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return config.getConfigSources();
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> aClass) {
        return config.getConverter(aClass);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return config.unwrap(aClass);
    }
}
