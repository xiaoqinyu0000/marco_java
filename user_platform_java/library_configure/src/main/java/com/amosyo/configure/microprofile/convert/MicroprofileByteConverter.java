package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileByteConverter extends AbstractConverter<Byte> {

    @Override
    protected @NonNull Byte doConvert(@NonNull String value) {
        return Byte.parseByte(value);
    }
}
