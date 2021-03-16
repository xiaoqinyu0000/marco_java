package com.amosyo.serv.user.configuration.microprofile.source;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MetaInfoMicroprofilePropertiesConfigSource extends AbstractConfigSource {

    public MetaInfoMicroprofilePropertiesConfigSource() {
        super();
    }

    @Override
    protected @NonNull Map<String, String> loadProperties() {
        final Properties prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/microprofile-config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.entrySet()
                .stream()
                .collect(toMap((entry) -> Objects.toString(entry.getKey(), ""), entry -> Objects.toString(entry.getValue(), ""), (o1, o2) -> o1));
    }

    @Override
    public int getOrdinal() {
        return 300;
    }

    @Override
    public String getName() {
        return "MATA-INF Properties";
    }
}
