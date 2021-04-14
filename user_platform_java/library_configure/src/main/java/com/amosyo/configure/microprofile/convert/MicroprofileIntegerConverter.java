package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileIntegerConverter extends AbstractConverter<Integer> {

    @Override
    protected @NonNull Integer doConvert(@NonNull String value) {
        return Integer.valueOf(value);
    }
}
