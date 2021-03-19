package com.amosyo.serv.user.configuration.microprofile.convert;

import com.amosyo.library.mvc.function.ThrowableCallable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import static java.util.Objects.isNull;
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
    private static final Converter<Boolean> CONVERTER_BOOLEAN = value -> ThrowableCallable.call(() -> Boolean.parseBoolean(requireNonNull(value, "value")), false, false);

    ApplicationConfigValueConvertManagerImpl() {
        this.doLoadConvertsMapFromSPI();
    }

    static final class Holder {

        static final ApplicationConfigValueConvertManagerImpl IMPL = new ApplicationConfigValueConvertManagerImpl();
    }

    private final Map<Class<?>, Converter<?>> converts = new HashMap<Class<?>, Converter<?>>(11, 1) {{
        put(Long.class, CONVERTER_LONG);
        put(long.class, CONVERTER_LONG);
        put(Integer.class, CONVERTER_INT);
        put(int.class, CONVERTER_INT);
        put(Float.class, CONVERTER_FLOAT);
        put(float.class, CONVERTER_FLOAT);
        put(Double.class, CONVERTER_DOUBLE);
        put(double.class, CONVERTER_DOUBLE);
        put(Boolean.class, CONVERTER_BOOLEAN);
        put(boolean.class, CONVERTER_BOOLEAN);
    }};

    private void doLoadConvertsMapFromSPI() {
        final ClassLoader classLoader = getClass().getClassLoader();
        ServiceLoader.load(Converter.class, classLoader)
                .forEach(converter -> getSuperClassGenericType(converter.getClass(), 0).ifPresent(aClass -> this.converts.put(aClass, converter)));
    }

    @NonNull
    private static Optional<Class<?>> getSuperClassGenericType(@NonNull final Class<?> clazz, final int index)
            throws IndexOutOfBoundsException {
        final Type[] types = Arrays.stream(clazz.getGenericInterfaces()).findFirst().filter(it -> it instanceof ParameterizedType)
                .map(it -> (ParameterizedType) it)
                .map(ParameterizedType::getActualTypeArguments)
                .orElse(null);

        if (isNull(types) || index >= types.length) {
            return Optional.empty();
        }

        return Optional.of(types[index])
                .filter(it -> it instanceof Class<?>)
                .map(it -> (Class<?>) it);
    }

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
}
