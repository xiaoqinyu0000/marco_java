package com.amosyo.serv.user.configuration.microprofile.source;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class JavaSystemEnvConfigSource extends AbstractConfigSource {


    public JavaSystemEnvConfigSource() {
        super();
    }

    @Override
    protected @NonNull Map<String, String> loadProperties() {
        return new HashMap<>(System.getenv());
    }

    @Override
    public int getOrdinal() {
        return 200;
    }

    @Override
    public String getName() {
        return "System Env Properties";
    }
}
