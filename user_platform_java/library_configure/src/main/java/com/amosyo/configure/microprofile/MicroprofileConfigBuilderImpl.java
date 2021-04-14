package com.amosyo.configure.microprofile;

import com.amosyo.configure.microprofile.source.AbstractConfigSource;
import com.amosyo.configure.microprofile.source.JavaSystemEnvConfigSource;
import com.amosyo.configure.microprofile.source.JavaSystemPropertiesConfigSource;
import com.amosyo.configure.microprofile.source.MetaInfoMicroprofilePropertiesConfigSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.ServiceLoader;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileConfigBuilderImpl implements ConfigBuilder {

    public static final int DEFAULT_PRIORITY = 100;

    private boolean mIsAddDefaultSources = false;
    private boolean mIsAddDiscoveredSources = false;
    private boolean mIsAddDiscoveredConverters = false;
    private ClassLoader mForClassLoader;
    private final List<ConfigSource> mConfigSourceList = new ArrayList<>();
    private final Map<Class<?>, PriorityQueue<PrioritizedConverter<?>>> mConverterQueueMap = new HashMap<>();

    @Override
    public ConfigBuilder addDefaultSources() {
        this.mIsAddDefaultSources = true;
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredSources() {
        this.mIsAddDiscoveredSources = true;
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredConverters() {
        this.mIsAddDiscoveredConverters = true;
        return this;
    }

    @Override
    public ConfigBuilder forClassLoader(ClassLoader loader) {
        this.mForClassLoader = loader;
        return this;
    }

    @Override
    public ConfigBuilder withSources(ConfigSource... sources) {
        this.mConfigSourceList.addAll(Arrays.asList(sources));
        return this;
    }

    @Override
    public ConfigBuilder withConverters(Converter<?>... converters) {
        if (0 == converters.length) {
            return this;
        }
        Arrays.stream(converters).forEach(converter -> {
            final Class<?> type = ConvertUtils.resolveConvertedType(converter);
            this.doAddConvert(converter, type, DEFAULT_PRIORITY);
        });
        return this;
    }

    @Override
    public <T> ConfigBuilder withConverter(Class<T> type, int priority, Converter<T> converter) {
        this.doAddConvert(converter, type, priority);
        return this;
    }

    private void doAddConvert(@NonNull final Converter<?> converter, @NonNull final Class<?> type, final int priority) {
        final PriorityQueue<PrioritizedConverter<?>> priorityQueue =
                this.mConverterQueueMap.computeIfAbsent(type, it -> new PriorityQueue<>());
        priorityQueue.offer(new PrioritizedConverter<>(converter, priority));
    }

    @Override
    public Config build() {
        if (this.mIsAddDefaultSources) {
            this.doAddDefaultSources();
        }
        if (this.mIsAddDiscoveredSources) {
            this.doAddDiscoveredSources();
        }
        if (this.mIsAddDiscoveredConverters) {
            this.doAddDiscoveredConverters();
        }
        return new MicroprofileConfigImpl(this.mConfigSourceList, this.mConverterQueueMap);
    }

    private void doAddDefaultSources() {
        this.doAddAndInitSourcesForClazz(JavaSystemPropertiesConfigSource.class);
        this.doAddAndInitSourcesForClazz(MetaInfoMicroprofilePropertiesConfigSource.class);
        this.doAddAndInitSourcesForClazz(JavaSystemEnvConfigSource.class);
    }

    private void doAddDiscoveredSources() {
        final ClassLoader classLoader = getClassLoaderNonNull();
        ServiceLoader.load(ConfigSource.class, classLoader).forEach(this.mConfigSourceList::add);
    }

    private void doAddAndInitSourcesForClazz(@NonNull final Class<?> clazz) {
        requireNonNull(clazz, "clazz");
        try {
            final ConfigSource configSource = (ConfigSource) clazz.newInstance();
            if (configSource instanceof AbstractConfigSource){
                ((AbstractConfigSource) configSource).initProperties();
            }
            this.mConfigSourceList.add(configSource);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void doAddDiscoveredConverters() {
        final ClassLoader classLoader = getClassLoaderNonNull();
        ServiceLoader.load(Converter.class, classLoader).forEach(this::withConverters);

    }

    @NonNull
    private ClassLoader getClassLoaderNonNull() {
        return Optional.ofNullable(this.mForClassLoader).orElse(Thread.currentThread().getContextClassLoader());
    }
}
