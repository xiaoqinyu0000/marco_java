package com.amosyo.serv.user.configuration.microprofile.convert;

import com.amosyo.library.mvc.function.ThrowableCallable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ApplicationConfigValueConvertManagerImpl implements ApplicationConfigValueConvertManager {

    private static final Converter<String> CONVERTER_STRING = value -> requireNonNull(value, "value");
    private static final Converter<Long> CONVERTER_LONG = value -> ThrowableCallable.call(() -> Long.parseLong(requireNonNull(value, "value")), false, 0L);
    private static final Converter<Integer> CONVERTER_INT = value -> ThrowableCallable.call(() -> Integer.parseInt(requireNonNull(value, "value")), false, 0);
    private static final Converter<Float> CONVERTER_FLOAT = value -> ThrowableCallable.call(() -> Float.parseFloat(requireNonNull(value, "value")), false, 0f);
    private static final Converter<Double> CONVERTER_DOUBLE = value -> ThrowableCallable.call(() -> Double.parseDouble(requireNonNull(value, "value")), false, 0.0d);

    ApplicationConfigValueConvertManagerImpl() {
    }

    static final class Holder {
        static final ApplicationConfigValueConvertManagerImpl IMPL = new ApplicationConfigValueConvertManagerImpl();
    }

    private final Map<Class<?>, Converter<?>> converts = new HashMap<Class<?>, Converter<?>>() {{
        put(String.class, CONVERTER_STRING);
        put(Long.class, CONVERTER_LONG);
        put(long.class, CONVERTER_LONG);
        put(Integer.class, CONVERTER_INT);
        put(int.class, CONVERTER_INT);
        put(Float.class, CONVERTER_FLOAT);
        put(float.class, CONVERTER_FLOAT);
        put(Double.class, CONVERTER_DOUBLE);
        put(double.class, CONVERTER_DOUBLE);
    }};


    @Override
    public void registerConvert(@NonNull final Class<?> clazz, @NonNull final Converter<?> converter) {
        this.converts.put(requireNonNull(clazz, "clazz"), requireNonNull(converter, "convert"));
    }

    @Override
    public void unRegisterConvert(@NonNull final Class<?> clazz) {
        this.converts.remove(requireNonNull(clazz, "clazz"));
    }

    @Override
    public <T> Optional<Converter<T>> getConvert(@NonNull Class<T> clazz) {
        requireNonNull(clazz, "clazz");
        return Optional.ofNullable(converts.get(clazz)).map(it -> (Converter<T>) it);
    }

    public static void main(String[] args) {

        System.out.println(CONVERTER_LONG.getClass());
    }
}
