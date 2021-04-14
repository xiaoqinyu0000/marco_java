package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileStringConverter extends AbstractConverter<String> {

    @Override
    protected @NonNull String doConvert(@NonNull String value) {
        return value;
    }
}
