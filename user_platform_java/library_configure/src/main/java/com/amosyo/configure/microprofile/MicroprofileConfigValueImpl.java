package com.amosyo.configure.microprofile;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileConfigValueImpl implements ConfigValue {

    private final ConfigSource mConfigSource;
    private final String mName;
    private final String mValue;

    public MicroprofileConfigValueImpl(
            @NonNull final ConfigSource configSource,
            @NonNull final String name,
            @NonNull final String value) {
        this.mConfigSource = requireNonNull(configSource, "configSource");
        this.mName = requireNonNull(name, "name");
        this.mValue = requireNonNull(value, "value");
    }

    @Override
    public String getName() {
        return this.mName;
    }

    @Override
    public String getValue() {
        return this.mValue;
    }

    @Override
    public String getRawValue() {
        return this.mValue;
    }

    @Override
    public String getSourceName() {
        return this.mConfigSource.getName();
    }

    @Override
    public int getSourceOrdinal() {
        return this.mConfigSource.getOrdinal();
    }
}
