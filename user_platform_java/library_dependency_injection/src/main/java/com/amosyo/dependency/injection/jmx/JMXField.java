package com.amosyo.library.mvc.jmx;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class JMXField {

    @NonNull
    private final Field field;
    @NonNull
    private final PropertyDescriptor propertyDescriptor;

    public JMXField(@NonNull final Field field, @NonNull final PropertyDescriptor propertyDescriptor) {
        this.field = field;
        this.propertyDescriptor = propertyDescriptor;
    }

    @NonNull
    public final Field getField() {
        return field;
    }

    @NonNull
    public final PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    @NonNull
    public Method[] getRwMethods() {
        final List<Method> methodList = new ArrayList<>(2);
        Optional.ofNullable(getPropertyDescriptor().getWriteMethod()).ifPresent(methodList::add);
        Optional.ofNullable(getPropertyDescriptor().getReadMethod()).ifPresent(methodList::add);
        return methodList.toArray(new Method[]{});
    }
}
