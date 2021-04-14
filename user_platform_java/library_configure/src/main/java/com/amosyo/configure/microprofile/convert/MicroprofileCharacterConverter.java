package com.amosyo.configure.microprofile.convert;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class MicroprofileCharacterConverter extends AbstractConverter<Character> {

    @Override
    protected @NonNull Character doConvert(@NonNull String value) {
        return value.charAt(0);
    }
}
