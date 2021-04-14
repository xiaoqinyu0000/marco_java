package com.amosyo.configure.microprofile.source;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class JavaSystemPropertiesConfigSource extends AbstractConfigSource {

    public JavaSystemPropertiesConfigSource() {
        super();
    }

    @Override
    protected @NonNull Map<String, String> loadProperties() {
        return System.getProperties()
                .entrySet()
                .stream()
                .collect(toMap((entry) -> Objects.toString(entry.getKey(), ""), entry -> Objects.toString(entry.getValue(), ""), (o1, o2) -> o1));
    }

    @Override
    public int getOrdinal() {
        return 100;
    }

    @Override
    public String getName() {
        return "Java System Properties";
    }
}
