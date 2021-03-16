package com.amosyo.serv.user.configuration.microprofile;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

import static java.util.Objects.isNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ApplicationConfigProviderResolver extends ConfigProviderResolver {

    private volatile Config config;

    @Override
    public Config getConfig() {
        return this.getConfig(null);
    }

    @Override
    public Config getConfig(ClassLoader classLoader) {
        if (isNull(this.config)) {
            synchronized (ApplicationConfigProviderResolver.class) {
                if (isNull(this.config)) {
                    final ClassLoader realClassLoader = Optional.ofNullable(classLoader).orElseGet(() -> Thread.currentThread().getContextClassLoader());
                    final Iterator<Config> iterator = ServiceLoader.load(Config.class, realClassLoader).iterator();
                    if (iterator.hasNext()) {
                        this.config = iterator.next();
                    } else {
                        throw new IllegalStateException("Error to Not Found Config implementation!");
                    }
                }
            }
        }
        return this.config;
    }

    @Override
    public ConfigBuilder getBuilder() {
        return null;
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {
        if (isNull(config)) {
            return;
        }
        this.config = config;
    }

    @Override
    public void releaseConfig(Config config) {
        if (Objects.equals(this.config, config)) {
            this.config = null;
        }
    }
}
