package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileBigDecimalConverter extends AbstractConverter<BigDecimal> {

    @Override
    protected @NonNull BigDecimal doConvert(@NonNull String value) {
        return new BigDecimal(value);
    }
}
