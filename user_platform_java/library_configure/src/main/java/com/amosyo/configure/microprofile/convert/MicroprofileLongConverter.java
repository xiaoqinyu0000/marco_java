package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.Converter;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileLongConverter extends AbstractConverter<Long> {

    @Override
    protected @NonNull Long doConvert(@NonNull String value) {
        return Long.parseLong(value);
    }
}
