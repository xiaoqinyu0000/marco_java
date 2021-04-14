package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileFloatConverter extends AbstractConverter<Float> {


    @Override
    protected @NonNull Float doConvert(@NonNull String value) {
        return Float.parseFloat(value);
    }
}
