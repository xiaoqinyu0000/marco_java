package com.amosyo.configure.microprofile;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ConvertUtils {

    @NonNull
    public static Class<?> resolveConvertedType(@NonNull final Converter<?> converter) {
        requireNonNull(converter, "converter");
        assertConverter(converter);
        Class<?> convertedType = null;
        Class<?> converterClass = converter.getClass();
        while (converterClass != null) {
            convertedType = resolveConvertedType(converterClass);
            if (convertedType != null) {
                break;
            }

            Type superType = converterClass.getGenericSuperclass();
            if (superType instanceof ParameterizedType) {
                convertedType = resolveConvertedType(superType);
            }

            if (convertedType != null) {
                break;
            }
            // recursively
            converterClass = converterClass.getSuperclass();

        }
        if (isNull(convertedType)){
            throw new IllegalArgumentException("The implementation class of Converter must have type ");
        }
        return convertedType;
    }

    private static void assertConverter(Converter<?> converter) {
        Class<?> converterClass = converter.getClass();
        if (converterClass.isInterface()) {
            throw new IllegalArgumentException("The implementation class of Converter must not be an interface!");
        }
        if (Modifier.isAbstract(converterClass.getModifiers())) {
            throw new IllegalArgumentException("The implementation class of Converter must not be abstract!");
        }
    }

    private static Class<?> resolveConvertedType(Class<?> converterClass) {
        Class<?> convertedType = null;

        for (Type superInterface : converterClass.getGenericInterfaces()) {
            convertedType = resolveConvertedType(superInterface);
            if (convertedType != null) {
                break;
            }
        }

        return convertedType;
    }

    private static Class<?> resolveConvertedType(Type type) {
        Class<?> convertedType = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType() instanceof Class) {
                Class<?> rawType = (Class) pType.getRawType();
                if (Converter.class.isAssignableFrom(rawType)) {
                    Type[] arguments = pType.getActualTypeArguments();
                    if (arguments.length == 1 && arguments[0] instanceof Class) {
                        convertedType = (Class) arguments[0];
                    }
                }
            }
        }
        return convertedType;
    }
}
