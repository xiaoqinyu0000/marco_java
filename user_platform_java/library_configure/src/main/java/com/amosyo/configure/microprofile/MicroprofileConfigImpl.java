package com.amosyo.configure.microprofile;

import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileConfigImpl implements Config {

    private final List<ConfigSource> mConfigSourceList = new ArrayList<>();
    private final Map<Class<?>, PriorityQueue<PrioritizedConverter<?>>> mConverterQueueMap = new HashMap<>();

    public MicroprofileConfigImpl(
            @NonNull final List<ConfigSource> configSourceList,
            @NonNull final Map<Class<?>, PriorityQueue<PrioritizedConverter<?>>> converterQueueMap) {
        requireNonNull(configSourceList, "configSourceList");
        requireNonNull(converterQueueMap, "converterQueueMap");
        mConfigSourceList.addAll(configSourceList);
        mConverterQueueMap.putAll(converterQueueMap);
        this.mConfigSourceList.sort(this::compareConfigSource);
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

        final Function<ConfigSource, Stream<ConfigSource>> findConfigSource = configSource ->
                Stream.of(configSource)
                        .map(cs -> cs.getValue(s))
                        .filter(StringUtils::isNotBlank)
                        .map(it -> configSource);

        return this.mConfigSourceList.stream()
                .flatMap(findConfigSource)
                .findFirst()
                .map(cs -> new MicroprofileConfigValueImpl(cs, s, cs.getValue(s))).orElse(null);
    }

    @Override
    public <T> List<T> getValues(String propertyName, Class<T> propertyType) {
        return this.mConfigSourceList.stream()
                .map(it -> it.getValue(propertyName))
                .filter(StringUtils::isNotBlank)
                .map(value -> doConvertByClazz(propertyType, value).orElse(null))
                .filter(value -> !Objects.isNull(value))
                .collect(Collectors.toList());
    }

    @Override
    public <T> Optional<T> getOptionalValue(String s, Class<T> aClass) {
        return Optional.ofNullable(this.getConfigValue(s))
                .flatMap(value -> doConvertByClazz(aClass, value.getValue()));
    }

    @NonNull
    private <T> Optional<T> doConvertByClazz(@NonNull final Class<T> aClass, @NonNull final String value) {
        return getConverter(aClass).map(convert -> convert.convert(value));
    }

    @Override
    public <T> Optional<List<T>> getOptionalValues(String propertyName, Class<T> propertyType) {
        return Optional.ofNullable(getValues(propertyName, propertyType))
                .filter(it -> !it.isEmpty());
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return this.mConfigSourceList.stream()
                .map(ConfigSource::getPropertyNames)
                .collect(HashSet::new, HashSet::addAll, HashSet::addAll);
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return Collections.unmodifiableCollection(this.mConfigSourceList);
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> aClass) {
        return Optional.ofNullable(mConverterQueueMap.get(aClass))
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(p -> (Converter<T>) p.getConvert());
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
