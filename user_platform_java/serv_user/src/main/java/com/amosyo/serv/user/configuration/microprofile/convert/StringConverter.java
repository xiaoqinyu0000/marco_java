package com.amosyo.serv.user.configuration.microprofile.convert;

import org.eclipse.microprofile.config.spi.Converter;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class StringConverter implements Converter<String> {

    @Override
    public String convert(String value) throws IllegalArgumentException, NullPointerException {
        return requireNonNull(value, "value");
    }
}
