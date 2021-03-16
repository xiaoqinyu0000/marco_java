package com.amosyo.serv.user.configuration.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.Optional;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public interface ApplicationConfigValueConvertManager {

    @NonNull
    static ApplicationConfigValueConvertManager getInstance() {
        return ApplicationConfigValueConvertManagerImpl.Holder.IMPL;
    }

    void registerConvert(@NonNull final Class<?> clazz, @NonNull final Converter<?> converter);

    void unRegisterConvert(@NonNull final Class<?> clazz);

    <T> Optional<Converter<T>> getConvert(@NonNull final Class<T> clazz);

}