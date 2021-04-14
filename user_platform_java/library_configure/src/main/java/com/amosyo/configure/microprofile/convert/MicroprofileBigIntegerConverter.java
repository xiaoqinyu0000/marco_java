package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigInteger;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileBigIntegerConverter extends AbstractConverter<BigInteger> {

    @Override
    protected @NonNull BigInteger doConvert(@NonNull String value) {
        return new BigInteger(value);
    }
}
