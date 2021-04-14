package com.amosyo.configure.microprofile;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.Converter;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class PrioritizedConverter<T> implements Converter<T>, Comparable<PrioritizedConverter<T>> {

    @NonNull
    private final Converter<T> mConvert;
    private final int priority;

    public PrioritizedConverter(@NonNull final Converter<T> convert, final int priority) {
        this.mConvert = requireNonNull(convert);
        this.priority = priority;
    }

    @NonNull
    public Converter<T> getConvert() {
        return mConvert;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public T convert(String value) throws IllegalArgumentException, NullPointerException {
        return this.mConvert.convert(value);
    }
    @Override
    public int compareTo(PrioritizedConverter<T> o) {
        requireNonNull(o, "can not compareTo Null Value");
        return Integer.compare(o.getPriority(), this.getPriority());
    }
}
