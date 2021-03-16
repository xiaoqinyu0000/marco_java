package com.amosyo.serv.user.configuration.microprofile;

import com.amosyo.serv.user.configuration.microprofile.convert.ApplicationConfigValueConvertManager;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Stream;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ApplicationConfig implements Config {

    private final List<ConfigSource> configSourceList = new ArrayList<>(2);

    public ApplicationConfig() {
        final ClassLoader classLoader = getClass().getClassLoader();
        ServiceLoader.load(ConfigSource.class, classLoader).forEach(this.configSourceList::add);
        this.configSourceList.sort(this::compareConfigSource);
        System.out.println(Arrays.toString(configSourceList.toArray()));
    }

    private int compareConfigSource(final @NonNull ConfigSource configSource1, final @NonNull ConfigSource configSource2) {
        return Integer.compare(configSource2.getOrdinal(), configSource1.getOrdinal());
    }

    @Override
    public <T> T getValue(String s, Class<T> aClass) {
        return getOptionalValue(s, aClass).orElse(null);
    }

    @Override
    public ConfigValue getConfigValue(String s) {
        return null;
    }

    @Override
    public <T> List<T> getValues(String propertyName, Class<T> propertyType) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String s, Class<T> aClass) {
        return this.configSourceList.stream().flatMap(configSource ->
                Stream.of(configSource).map(cs -> cs.getValue(s)).filter(StringUtils::isNotBlank)
        ).findFirst().flatMap(value -> getConverter(aClass).map(convert -> convert.convert(value)));
    }

    @Override
    public <T> Optional<List<T>> getOptionalValues(String propertyName, Class<T> propertyType) {
        return Optional.empty();
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return null;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return Collections.unmodifiableCollection(this.configSourceList);
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> aClass) {
        return ApplicationConfigValueConvertManager.getInstance().getConvert(aClass);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
