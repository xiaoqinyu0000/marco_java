package com.amosyo.configure.microprofile;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileConfigProviderResolverImpl extends ConfigProviderResolver {

    private final Map<ClassLoader, Config> mClassLoad2ConfigMap = new ConcurrentHashMap<>();

    @Override
    public Config getConfig() {
        return this.getConfig(null);
    }

    @Override
    public Config getConfig(ClassLoader classLoader) {
        if (isNull(classLoader)) {
            classLoader = this.getClass().getClassLoader();
        }
        return this.mClassLoad2ConfigMap.computeIfAbsent(classLoader, this::newConfig);
    }

    private Config newConfig(ClassLoader classLoader) {
        return getBuilder().forClassLoader(classLoader).build();
    }

    @Override
    public ConfigBuilder getBuilder() {
        return new MicroprofileConfigBuilderImpl();
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {
        if (isNull(config)) {
            return;
        }
        if (isNull(classLoader)) {
            return;
        }
        this.mClassLoad2ConfigMap.putIfAbsent(classLoader, config);
    }

    @Override
    public void releaseConfig(Config config) {
        if (isNull(config)) {
            return;
        }
        this.mClassLoad2ConfigMap.entrySet()
                .stream()
                .filter(classLoaderConfigEntry -> Objects.equals(classLoaderConfigEntry.getValue(), config))
                .map(Map.Entry::getKey)
                .forEach(this.mClassLoad2ConfigMap::remove);
    }
}
