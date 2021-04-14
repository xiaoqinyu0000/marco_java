package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileDoubleConverter extends AbstractConverter<Double> {

    @Override
    protected @NonNull Double doConvert(@NonNull String value) {
        return Double.parseDouble(value);
    }
}
