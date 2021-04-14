package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.Converter;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public abstract class AbstractConverter<T> implements Converter<T> {

    @Override
    public T convert(String value) throws IllegalArgumentException, NullPointerException {
        final T theConverted = doConvert(requireNonNull(value, "the value must is not null"));
        return requireNonNull(theConverted, "converted is null, value is " + value);
    }

    @NonNull
    protected abstract T doConvert(@NonNull final String value);
}
